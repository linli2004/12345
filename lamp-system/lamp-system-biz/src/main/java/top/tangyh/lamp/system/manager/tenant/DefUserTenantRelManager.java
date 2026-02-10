package top.tangyh.lamp.system.manager.tenant;

import top.tangyh.basic.base.manager.SuperCacheManager;
import top.tangyh.lamp.system.entity.tenant.DefUserTenantRel;
import top.tangyh.lamp.system.vo.result.tenant.DefUserTenantRelResultVO;

import java.util.List;

/**
 * <p>
 * 通用业务接口
 * 员工
 * </p>
 *
 * @author zuihou
 * @date 2021-10-27
 */
public interface DefUserTenantRelManager extends SuperCacheManager<DefUserTenantRel> {
    /**
     * 根据用户id查询员工
     *
     * @param userId 用户id
     * @return
     */
    List<DefUserTenantRelResultVO> listEmployeeByUserId(Long userId);
}
