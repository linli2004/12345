package top.tangyh.lamp.sop.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.sop.entity.SopPermGroupPermission;
import top.tangyh.lamp.sop.manager.SopPermGroupPermissionManager;
import top.tangyh.lamp.sop.mapper.SopPermGroupPermissionMapper;

/**
 * <p>
 * 通用业务实现类
 * 组权限表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SopPermGroupPermissionManagerImpl extends SuperManagerImpl<SopPermGroupPermissionMapper, SopPermGroupPermission> implements SopPermGroupPermissionManager {

}


