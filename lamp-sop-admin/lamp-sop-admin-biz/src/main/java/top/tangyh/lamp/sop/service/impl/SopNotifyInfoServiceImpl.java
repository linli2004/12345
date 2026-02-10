package top.tangyh.lamp.sop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.update.LbUpdateWrap;
import top.tangyh.lamp.sop.entity.SopNotifyInfo;
import top.tangyh.lamp.sop.enums.NotifyStatusEnum;
import top.tangyh.lamp.sop.manager.SopNotifyInfoManager;
import top.tangyh.lamp.sop.service.NotifyService;
import top.tangyh.lamp.sop.service.SopNotifyInfoService;

/**
 * <p>
 * 业务实现类
 * 回调信息
 * </p>
 *
 * @author zuihou
 * @date 2025-12-17 15:38:07
 * @create [2025-12-17 15:38:07] [zuihou] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SopNotifyInfoServiceImpl extends SuperServiceImpl<SopNotifyInfoManager, Long, SopNotifyInfo> implements SopNotifyInfoService {
    private final NotifyService notifyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean end(Long id) {
        LbUpdateWrap<SopNotifyInfo> eq = Wraps.<SopNotifyInfo>lbU()
                .set(SopNotifyInfo::getNotifyStatus, NotifyStatusEnum.END.getCode())
                .eq(SopNotifyInfo::getNotifyStatus, NotifyStatusEnum.SEND_FAIL.getCode())
                .eq(SopNotifyInfo::getId, id);
        return superManager.update(eq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long push(Long id, String url) {
        if (StringUtils.isNotBlank(url)) {
            LbUpdateWrap<SopNotifyInfo> eq = Wraps.<SopNotifyInfo>lbU()
                    .set(SopNotifyInfo::getNotifyUrl, url)
                    .eq(SopNotifyInfo::getId, id);
            superManager.update(eq);
        }
        Long notifyId = notifyService.notifyImmediately(id);
        log.info("重新推送结果, notifyId={}", notifyId);
        return notifyId;
    }
}


