package top.tangyh.lamp.base.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.lamp.base.entity.ChiefWorkOrderDynamic;
import top.tangyh.lamp.base.entity.WorkOrderTemporary;
import top.tangyh.lamp.base.manager.ChiefWorkOrderDynamicManager;
import top.tangyh.lamp.base.manager.WorkOrderTemporaryManager;
import top.tangyh.lamp.base.service.ChiefWorkOrderDynamicService;
import top.tangyh.lamp.common.constant.DsConstant;

/**
 * <p>
 * 业务实现类
 * 督办工单办理动态
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@Service
@DS(DsConstant.BASE_TENANT)
@RequiredArgsConstructor
public class ChiefWorkOrderDynamicServiceImpl extends SuperServiceImpl<ChiefWorkOrderDynamicManager, Long, ChiefWorkOrderDynamic> implements ChiefWorkOrderDynamicService {
    private final WorkOrderTemporaryManager workOrderTemporaryManager;

    @Override
    protected <SaveVO> void saveAfter(SaveVO saveVO, ChiefWorkOrderDynamic entity) {
        workOrderTemporaryManager.remove(Wraps.<WorkOrderTemporary>lbQ()
                .eq(WorkOrderTemporary::getOrderNo, entity.getOrderNo())
                .eq(WorkOrderTemporary::getOperatorId, entity.getOperatorId())
                .like(WorkOrderTemporary::getNodeName, "批示"));
    }
}
