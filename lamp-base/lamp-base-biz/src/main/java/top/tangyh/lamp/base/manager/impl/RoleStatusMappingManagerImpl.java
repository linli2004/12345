package top.tangyh.lamp.base.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.base.entity.RoleStatusMapping;
import top.tangyh.lamp.base.manager.RoleStatusMappingManager;
import top.tangyh.lamp.base.mapper.RoleStatusMappingMapper;

/**
 * <p>
 * 通用业务实现类
 * 角色视图映射配置表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:49:32
 * @create [2026-03-03 11:49:32] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RoleStatusMappingManagerImpl extends SuperManagerImpl<RoleStatusMappingMapper, RoleStatusMapping> implements RoleStatusMappingManager {

}


