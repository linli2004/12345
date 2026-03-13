package top.tangyh.lamp.base.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.lamp.base.entity.WorkOrderDynamic;
import top.tangyh.lamp.base.entity.WorkOrderTemporary;
import top.tangyh.lamp.base.manager.WorkOrderDynamicManager;
import top.tangyh.lamp.base.manager.WorkOrderTemporaryManager;
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
public class WorkOrderDynamicServiceImpl extends SuperServiceImpl<WorkOrderDynamicManager, Long, WorkOrderDynamic> implements WorkOrderDynamicService {
    private final WorkOrderTemporaryManager workOrderTemporaryManager;

    @Override
    protected <SaveVO> void saveAfter(SaveVO saveVO, WorkOrderDynamic entity) {
        workOrderTemporaryManager.remove(Wraps.<WorkOrderTemporary>lbQ()
                .eq(WorkOrderTemporary::getOrderNo, entity.getOrderNo())
                .eq(WorkOrderTemporary::getOperatorId, entity.getOperatorId())
                .like(WorkOrderTemporary::getNodeName, "批示"));
    }
}


