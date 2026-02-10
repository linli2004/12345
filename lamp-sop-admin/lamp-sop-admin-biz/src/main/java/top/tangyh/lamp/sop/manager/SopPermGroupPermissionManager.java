package top.tangyh.lamp.sop.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.basic.utils.SpringUtils;
import top.tangyh.lamp.sop.entity.SopPermGroupPermission;
import top.tangyh.lamp.sop.event.ChangeIsvPermEvent;

import java.util.List;

/**
 * <p>
 * 通用业务接口
 * 组权限表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
public interface SopPermGroupPermissionManager extends SuperManager<SopPermGroupPermission> {
    default void refreshIsvPerm(Long groupId) {
        List<SopPermGroupPermission> list = list(Wrappers.<SopPermGroupPermission>lambdaQuery().eq(SopPermGroupPermission::getGroupId, groupId));

        SpringUtils.publishEvent(new ChangeIsvPermEvent(list.stream().map(SopPermGroupPermission::getGroupId).distinct().toList()));
    }

}


