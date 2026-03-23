package top.tangyh.lamp.base.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.base.entity.WorkOrderDynamic;
import top.tangyh.lamp.base.manager.WorkOrderDynamicManager;
import top.tangyh.lamp.base.mapper.WorkOrderDynamicMapper;

import java.util.List;

/**
 * <p>
 * 通用业务实现类
 * 工单办理动态
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:48:11
 * @create [2026-03-03 11:48:11] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WorkOrderDynamicManagerImpl extends SuperManagerImpl<WorkOrderDynamicMapper, WorkOrderDynamic> implements WorkOrderDynamicManager {

    @Override
    public List<WorkOrderDynamic> getLastOperateTimeByOrderNo(List<String> orderNoList) {
        return baseMapper.getLastOperateTimeByOrderNo(orderNoList);
    }
}


