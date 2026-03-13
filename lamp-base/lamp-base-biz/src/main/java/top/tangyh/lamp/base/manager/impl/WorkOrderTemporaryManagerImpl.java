package top.tangyh.lamp.base.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.base.entity.WorkOrderTemporary;
import top.tangyh.lamp.base.manager.WorkOrderTemporaryManager;
import top.tangyh.lamp.base.mapper.WorkOrderTemporaryMapper;

/**
 * <p>
 * 通用业务实现类
 * 工单办理暂存
 * </p>
 *
 * @author lunar
 * @date 2026-03-12 11:50:36
 * @create [2026-03-12 11:50:36] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WorkOrderTemporaryManagerImpl extends SuperManagerImpl<WorkOrderTemporaryMapper, WorkOrderTemporary> implements WorkOrderTemporaryManager {

}


