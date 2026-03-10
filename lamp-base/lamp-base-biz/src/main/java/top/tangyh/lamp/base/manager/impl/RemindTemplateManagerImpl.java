package top.tangyh.lamp.base.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.base.entity.RemindTemplate;
import top.tangyh.lamp.base.manager.RemindTemplateManager;
import top.tangyh.lamp.base.mapper.RemindTemplateMapper;

/**
 * <p>
 * 通用业务实现类
 * 提醒模板
 * </p>
 *
 * @author lunar
 * @date 2026-03-10 08:39:01
 * @create [2026-03-10 08:39:01] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RemindTemplateManagerImpl extends SuperManagerImpl<RemindTemplateMapper, RemindTemplate> implements RemindTemplateManager {

}


