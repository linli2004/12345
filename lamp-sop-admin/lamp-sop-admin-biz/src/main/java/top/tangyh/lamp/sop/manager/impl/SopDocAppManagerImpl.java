package top.tangyh.lamp.sop.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.sop.entity.SopDocApp;
import top.tangyh.lamp.sop.manager.SopDocAppManager;
import top.tangyh.lamp.sop.mapper.SopDocAppMapper;

/**
 * <p>
 * 通用业务实现类
 * 文档应用
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SopDocAppManagerImpl extends SuperManagerImpl<SopDocAppMapper, SopDocApp> implements SopDocAppManager {

}


