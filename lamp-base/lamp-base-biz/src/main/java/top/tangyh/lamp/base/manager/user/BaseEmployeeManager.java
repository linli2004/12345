package top.tangyh.lamp.base.manager.user;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import top.tangyh.basic.base.manager.SuperCacheManager;
import top.tangyh.lamp.base.entity.user.BaseEmployee;
import top.tangyh.lamp.base.vo.query.user.BaseEmployeePageQuery;
import top.tangyh.lamp.base.vo.result.user.BaseEmployeeResultVO;

import java.util.List;

/**
 * <p>
 * 通用业务接口
 * 员工
 * </p>
 *
 * @author zuihou
 * @date 2021-10-18
 */
public interface BaseEmployeeManager extends SuperCacheManager<BaseEmployee> {
    /**
     * 分页查询
     *
     * @param page    分页对象
     * @param wrapper 查询条件
     * @param model   参数
     * @return 分页用户数据
     */
    IPage<BaseEmployeeResultVO> selectPageResultVO(IPage<BaseEmployee> page, Wrapper<BaseEmployee> wrapper, BaseEmployeePageQuery model);

    List<BaseEmployeeResultVO> getEmployeeIdByRoleCodeAndOrgId(String roleCode, Long orgId);
}
