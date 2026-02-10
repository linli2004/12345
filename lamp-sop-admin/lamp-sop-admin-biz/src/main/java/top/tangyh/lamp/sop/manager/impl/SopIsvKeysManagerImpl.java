package top.tangyh.lamp.sop.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.sop.entity.SopIsvKeys;
import top.tangyh.lamp.sop.manager.SopIsvKeysManager;
import top.tangyh.lamp.sop.mapper.SopIsvKeysMapper;

/**
 * <p>
 * 通用业务实现类
 * ISV秘钥管理
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SopIsvKeysManagerImpl extends SuperManagerImpl<SopIsvKeysMapper, SopIsvKeys> implements SopIsvKeysManager {

}


