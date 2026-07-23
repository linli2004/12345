package top.tangyh.lamp.base.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.base.entity.ChiefWorkOrder;
import top.tangyh.lamp.base.mapper.ChiefWorkOrderMapper;
import top.tangyh.lamp.base.manager.ChiefWorkOrderManager;

/**
 * <p>
 * 通用业务实现类
 * 督办工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChiefWorkOrderManagerImpl extends SuperManagerImpl<ChiefWorkOrderMapper, ChiefWorkOrder> implements ChiefWorkOrderManager {

}
