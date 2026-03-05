package top.tangyh.lamp.base.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.base.entity.WorkOrderDynamic;
import top.tangyh.lamp.base.manager.WorkOrderDynamicManager;
import top.tangyh.lamp.base.service.WorkOrderDynamicService;
import top.tangyh.lamp.common.constant.DsConstant;

/**
 * <p>
 * 业务实现类
 * 工单办理动态
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:48:11
 * @create [2026-03-03 11:48:11] [lunar] [代码生成器生成]
 */
@DS(DsConstant.BASE_TENANT)
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class WorkOrderDynamicServiceImpl extends SuperServiceImpl<WorkOrderDynamicManager, Long, WorkOrderDynamic> implements WorkOrderDynamicService {


}


