package top.tangyh.lamp.base.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.base.entity.NormalWorkOrderTask;
import top.tangyh.lamp.base.manager.NormalWorkOrderTaskManager;
import top.tangyh.lamp.base.mapper.NormalWorkOrderTaskMapper;

/**
 * <p>
 * 通用业务实现类
 * 工单子表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:45:59
 * @create [2026-03-03 11:45:59] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NormalWorkOrderTaskManagerImpl extends SuperManagerImpl<NormalWorkOrderTaskMapper, NormalWorkOrderTask> implements NormalWorkOrderTaskManager {

}


