package top.tangyh.lamp.base.manager;

import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.lamp.base.entity.WorkOrderDynamic;

import java.util.List;

/**
 * <p>
 * 通用业务接口
 * 工单办理动态
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:48:11
 * @create [2026-03-03 11:48:11] [lunar] [代码生成器生成]
 */
public interface WorkOrderDynamicManager extends SuperManager<WorkOrderDynamic> {
    List<WorkOrderDynamic> getLastOperateTimeByOrderNo(List<String> orderNoList);
}


