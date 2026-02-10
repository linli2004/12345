package top.tangyh.lamp.msg.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.lamp.msg.entity.ExtendMsgTemplate;
import top.tangyh.lamp.msg.manager.ExtendMsgTemplateManager;
import top.tangyh.lamp.msg.mapper.ExtendMsgTemplateMapper;

/**
 * <p>
 * 通用业务实现类
 * 消息模板
 * </p>
 *
 * @author zuihou
 * @date 2022-07-04 15:51:37
 * @create [2022-07-04 15:51:37] [zuihou] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ExtendMsgTemplateManagerImpl extends SuperManagerImpl<ExtendMsgTemplateMapper, ExtendMsgTemplate> implements ExtendMsgTemplateManager {
    @Override
    public ExtendMsgTemplate getByCode(String code) {
        return getOne(Wraps.<ExtendMsgTemplate>lbQ().eq(ExtendMsgTemplate::getCode, code));
    }
}


