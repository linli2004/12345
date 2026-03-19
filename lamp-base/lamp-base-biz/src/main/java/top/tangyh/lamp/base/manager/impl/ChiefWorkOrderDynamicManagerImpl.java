package top.tangyh.lamp.base.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.base.entity.ChiefWorkOrderDynamic;
import top.tangyh.lamp.base.mapper.ChiefWorkOrderDynamicMapper;
import top.tangyh.lamp.base.manager.ChiefWorkOrderDynamicManager;

/**
 * <p>
 * 通用业务实现类
 * 督办工单办理动态
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChiefWorkOrderDynamicManagerImpl extends SuperManagerImpl<ChiefWorkOrderDynamicMapper, ChiefWorkOrderDynamic> implements ChiefWorkOrderDynamicManager {

}
