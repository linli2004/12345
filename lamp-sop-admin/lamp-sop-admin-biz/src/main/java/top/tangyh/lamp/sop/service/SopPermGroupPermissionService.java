package top.tangyh.lamp.sop.service;

import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.sop.entity.SopPermGroupPermission;

import java.util.List;


/**
 * <p>
 * 业务接口
 * 组权限表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
public interface SopPermGroupPermissionService extends SuperService<Long, SopPermGroupPermission> {

    Boolean delete(Long groupId, List<Long> apiIdList);
}


