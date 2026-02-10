package top.tangyh.lamp.msg.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.msg.entity.ExtendMsgTemplate;
import top.tangyh.lamp.msg.manager.ExtendMsgTemplateManager;
import top.tangyh.lamp.msg.service.ExtendMsgTemplateService;
import top.tangyh.lamp.msg.vo.save.ExtendMsgTemplateSaveVO;
import top.tangyh.lamp.msg.vo.update.ExtendMsgTemplateUpdateVO;

/**
 * <p>
 * 业务实现类
 * 消息模板
 * </p>
 *
 * @author zuihou
 * @date 2022-07-04 15:51:37
 * @create [2022-07-04 15:51:37] [zuihou] [代码生成器生成]
 */
@DS(DsConstant.BASE_TENANT)
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ExtendMsgTemplateServiceImpl extends SuperServiceImpl<ExtendMsgTemplateManager, Long, ExtendMsgTemplate> implements ExtendMsgTemplateService {
    @Override
    public ExtendMsgTemplate getByCode(String code) {
        return superManager.getByCode(code);
    }

    @Override
    public Boolean check(String code, Long id) {
        ArgumentAssert.notEmpty(code, "请填写模板标识");
        return superManager.count(Wraps.<ExtendMsgTemplate>lbQ().eq(ExtendMsgTemplate::getCode, code)
                .ne(ExtendMsgTemplate::getId, id)) > 0;
    }

    @Override
    protected <SaveVO> ExtendMsgTemplate saveBefore(SaveVO saveVO) {
        ExtendMsgTemplateSaveVO extendMsgTemplateSaveVO = (ExtendMsgTemplateSaveVO) saveVO;
        ArgumentAssert.isFalse(StrUtil.isNotBlank(extendMsgTemplateSaveVO.getCode()) &&
                               check(extendMsgTemplateSaveVO.getCode(), null), "模板标识{}已存在", extendMsgTemplateSaveVO.getCode());

        return super.saveBefore(extendMsgTemplateSaveVO);
    }

    @Override
    protected <UpdateVO> ExtendMsgTemplate updateBefore(UpdateVO updateVO) {
        ExtendMsgTemplateUpdateVO extendMsgTemplateUpdateVO = (ExtendMsgTemplateUpdateVO) updateVO;
        ArgumentAssert.isFalse(StrUtil.isNotBlank(extendMsgTemplateUpdateVO.getCode()) &&
                               check(extendMsgTemplateUpdateVO.getCode(), extendMsgTemplateUpdateVO.getId()),
                "模板标识{}已存在", extendMsgTemplateUpdateVO.getCode());

        return super.updateBefore(extendMsgTemplateUpdateVO);
    }
}


