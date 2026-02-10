package top.tangyh.lamp.sop.manager;

import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.lamp.sop.entity.SopPermIsvGroup;

import java.util.List;

/**
 * <p>
 * 通用业务接口
 * isv分组
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
public interface SopPermIsvGroupManager extends SuperManager<SopPermIsvGroup> {
    List<Long> listGroupIdByIsvId(Long isvId);
}


