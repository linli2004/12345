package top.tangyh.lamp.base.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.base.entity.RemindTemplate;
import top.tangyh.lamp.base.manager.RemindTemplateManager;
import top.tangyh.lamp.base.service.RemindTemplateService;
import top.tangyh.lamp.common.constant.DsConstant;

/**
 * <p>
 * 业务实现类
 * 提醒模板
 * </p>
 *
 * @author lunar
 * @date 2026-03-10 08:39:01
 * @create [2026-03-10 08:39:01] [lunar] [代码生成器生成]
 */
@DS(DsConstant.BASE_TENANT)
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RemindTemplateServiceImpl extends SuperServiceImpl<RemindTemplateManager, Long, RemindTemplate> implements RemindTemplateService {


}


