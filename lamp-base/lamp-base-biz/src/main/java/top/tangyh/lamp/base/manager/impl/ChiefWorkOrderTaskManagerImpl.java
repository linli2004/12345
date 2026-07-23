package top.tangyh.lamp.base.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.base.entity.ChiefWorkOrderTask;
import top.tangyh.lamp.base.mapper.ChiefWorkOrderTaskMapper;
import top.tangyh.lamp.base.manager.ChiefWorkOrderTaskManager;

/**
 * <p>
 * 通用业务实现类
 * 督办工单子表
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChiefWorkOrderTaskManagerImpl extends SuperManagerImpl<ChiefWorkOrderTaskMapper, ChiefWorkOrderTask> implements ChiefWorkOrderTaskManager {

}
