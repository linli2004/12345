package top.tangyh.lamp.sop.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.exception.BizException;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.model.enumeration.AuditStatusEnum;
import top.tangyh.lamp.model.enumeration.StateEnum;
import top.tangyh.lamp.model.vo.save.AudioVO;
import top.tangyh.lamp.model.vo.save.StatusUpdateVO;
import top.tangyh.lamp.sop.entity.SopIsvInfo;
import top.tangyh.lamp.sop.entity.SopIsvKeys;
import top.tangyh.lamp.sop.entity.SopPermIsvGroup;
import top.tangyh.lamp.sop.enums.CreationMethodEnum;
import top.tangyh.lamp.sop.manager.SopIsvInfoManager;
import top.tangyh.lamp.sop.manager.SopIsvKeysManager;
import top.tangyh.lamp.sop.manager.SopPermIsvGroupManager;
import top.tangyh.lamp.sop.service.SopIsvInfoService;
import top.tangyh.lamp.sop.service.SopIsvKeysService;
import top.tangyh.lamp.sop.utils.RSATool;
import top.tangyh.lamp.sop.vo.result.SopIsvKeysResultVO;
import top.tangyh.lamp.sop.vo.save.SopIsvInfoApplyForVO;
import top.tangyh.lamp.sop.vo.save.SopIsvInfoSubmitVO;
import top.tangyh.lamp.sop.vo.update.SopIsvInfoOpenUpdateVO;
import top.tangyh.lamp.sop.vo.update.SopIsvKeysUpdateVO;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 业务实现类
 * isv信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SopIsvInfoServiceImpl extends SuperServiceImpl<SopIsvInfoManager, Long, SopIsvInfo> implements SopIsvInfoService {

    private final UidGenerator uidGenerator;
    private final SopIsvKeysManager sopIsvKeysManager;
    private final SopPermIsvGroupManager sopPermIsvGroupManager;
    private final SopIsvKeysService sopIsvKeysService;

    @Override
    protected <SaveVO> SopIsvInfo saveBefore(SaveVO saveVO) {
        SopIsvInfo sopIsvInfo = super.saveBefore(saveVO);
        String appId = new SimpleDateFormat("yyyyMMdd").format(new Date()) + uidGenerator.getUid();
        sopIsvInfo.setAppId(appId);
        sopIsvInfo.setStatus(StateEnum.ENABLE.getInteger());
        sopIsvInfo.setCreationMethod(CreationMethodEnum.CREATE.getVal());
        sopIsvInfo.setAuditStatus(AuditStatusEnum.PASS.getVal());
        return sopIsvInfo;
    }

    @Override
    protected <SaveVO> void saveAfter(SaveVO saveVO, SopIsvInfo entity) {
        superManager.sendChangeEvent(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <UpdateVO> SopIsvInfo updateById(UpdateVO updateVO) {
        SopIsvInfo entity = BeanUtil.toBean(updateVO, SopIsvInfo.class);

        LambdaUpdateWrapper<SopIsvInfo> updateWrapper = Wrappers.<SopIsvInfo>lambdaUpdate().set(SopIsvInfo::getRemark, entity.getRemark())
                .set(SopIsvInfo::getStatus, entity.getStatus())
                .set(SopIsvInfo::getNotifyUrl, entity.getNotifyUrl())
                .set(SopIsvInfo::getStartExpirationTime, entity.getStartExpirationTime())
                .set(SopIsvInfo::getEndExpirationTime, entity.getEndExpirationTime())
                .set(SopIsvInfo::getName, entity.getName())
                .eq(SopIsvInfo::getId, entity.getId());
        superManager.update(updateWrapper);

        superManager.sendChangeEvent(entity.getId());
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<Long> idList) {
        boolean cnt = super.removeByIds(idList);
        sopIsvKeysManager.remove(Wrappers.<SopIsvKeys>lambdaQuery().in(SopIsvKeys::getIsvId, idList));
        sopPermIsvGroupManager.remove(Wrappers.<SopPermIsvGroup>lambdaQuery().in(SopPermIsvGroup::getIsvId, idList));
        superManager.sendChangeEvent(idList);
        return cnt;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStatus(StatusUpdateVO param) {
        boolean cnt = superManager.update(Wrappers.<SopIsvInfo>lambdaUpdate().set(SopIsvInfo::getStatus, param.getStatus()).eq(SopIsvInfo::getId, param.getId()));
        superManager.sendChangeEvent(param.getId());
        return cnt;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public Long examine(AudioVO param) {
        SopIsvInfo isv = this.getById(param.getId());
        if (isv == null) {
            throw new BizException("您要审核的数据不存在");
        }
        if (AuditStatusEnum.PASS.getVal().equals(param.getAuditStatus())) {
            isv.setStatus(StateEnum.ENABLE.getInteger());
        }
        isv.setAuditStatus(param.getAuditStatus());
        isv.setAuditTime(LocalDateTime.now());
        isv.setReviewComments(param.getReviewComments());
        superManager.updateById(isv);

        // 同步isv信息
        superManager.sendChangeEvent(isv.getId());

        if (AuditStatusEnum.PASS.getVal().equals(param.getAuditStatus())) {
            RSATool.KeyStore platformKeys = createKeys(RSATool.KeyFormat.PKCS8);
            RSATool.KeyStore isvKeys = createKeys(RSATool.KeyFormat.PKCS8);

            SopIsvKeysUpdateVO keysUpdateVO = new SopIsvKeysUpdateVO();
            keysUpdateVO.setIsvId(isv.getId());
            keysUpdateVO.setKeyFormat(RSATool.KeyFormat.PKCS8.getValue());
            keysUpdateVO.setPrivateKeyIsv(isvKeys.getPrivateKey());
            keysUpdateVO.setPublicKeyIsv(isvKeys.getPublicKey());
            keysUpdateVO.setPrivateKeyPlatform(platformKeys.getPrivateKey());
            keysUpdateVO.setPublicKeyPlatform(platformKeys.getPublicKey());
            sopIsvKeysService.saveKeys(keysUpdateVO);
        }

        return isv.getId();
    }

    @Override
    public RSATool.KeyStore createKeys(RSATool.KeyFormat keyFormat) throws Exception {
        if (keyFormat == null) {
            keyFormat = RSATool.KeyFormat.PKCS8;
        }
        RSATool rsaTool = new RSATool(keyFormat, RSATool.KeyLength.LENGTH_2048);
        return rsaTool.createKeys();
    }

    @Override
    public SopIsvKeysResultVO getKeys(Long isvId) {
        SopIsvInfo isvInfo = this.getById(isvId);
        ArgumentAssert.notNull(isvInfo, "ISV不存在");
        SopIsvKeys isvKeys = sopIsvKeysService.getByIsvId(isvId);

        SopIsvKeysResultVO isvKeysDTO;
        if (isvKeys == null) {
            isvKeysDTO = new SopIsvKeysResultVO();
            isvKeysDTO.setIsvId(isvId);
            isvKeysDTO.setKeyFormat(RSATool.KeyFormat.PKCS8.getValue());
            isvKeysDTO.setPublicKeyIsv("");
            isvKeysDTO.setPrivateKeyIsv("");
            isvKeysDTO.setPublicKeyPlatform("");
            isvKeysDTO.setPrivateKeyPlatform("");
        } else {
            isvKeysDTO = BeanUtil.copyProperties(isvKeys, SopIsvKeysResultVO.class);
        }
        isvKeysDTO.setAppId(isvInfo.getAppId());
        return isvKeysDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long applyFor(SopIsvInfoApplyForVO param) {
        SopIsvInfo bean = BeanUtil.toBean(param, SopIsvInfo.class);
        bean.setStatus(StateEnum.DISABLE.getInteger());
        bean.setCreationMethod(CreationMethodEnum.APPLY_FOR.getVal());
        bean.setAuditStatus(AuditStatusEnum.INIT.getVal());
        bean.setTenantId(ContextUtil.getTenantId());
        String appKey = new SimpleDateFormat("yyyyMMdd").format(new Date()) + uidGenerator.getUid();
        bean.setAppId(appKey);
        superManager.save(bean);
        return bean.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(SopIsvInfoOpenUpdateVO param) {
        SopIsvInfo bean = BeanUtil.toBean(param, SopIsvInfo.class);
        superManager.updateById(bean);
        return bean.getId();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submit(SopIsvInfoSubmitVO param) {
        SopIsvInfo isv = this.getById(param.getId());
        if (isv == null) {
            throw new BizException("您提交的申请不存在");
        }
        isv.setName(param.getName());
        isv.setRemark(param.getRemark());
        isv.setAuditStatus(AuditStatusEnum.APPLYING.getVal());
        isv.setNotifyUrl(param.getNotifyUrl());
        isv.setSubmissionTime(LocalDateTime.now());
        superManager.updateById(isv);
        return isv.getId();
    }
}


