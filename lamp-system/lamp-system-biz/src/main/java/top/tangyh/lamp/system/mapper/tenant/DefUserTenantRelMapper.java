package top.tangyh.lamp.system.mapper.tenant;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.tangyh.basic.base.mapper.SuperMapper;
import top.tangyh.lamp.system.entity.tenant.DefUserTenantRel;
import top.tangyh.lamp.system.vo.result.tenant.DefUserTenantRelResultVO;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * 员工
 * </p>
 *
 * @author zuihou
 * @date 2021-10-27
 */
@Repository
@InterceptorIgnore(tenantLine = "true", dynamicTableName = "true")
public interface DefUserTenantRelMapper extends SuperMapper<DefUserTenantRel> {
    /**
     * 根据用户id查询员工
     *
     * @param userId 用户id
     * @return
     */
    List<DefUserTenantRelResultVO> listEmployeeByUserId(@Param("userId") Long userId);
}
