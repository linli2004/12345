package top.tangyh.lamp.system.mapper.tenant;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.tangyh.basic.base.mapper.SuperMapper;
import top.tangyh.lamp.system.entity.tenant.DefTenant;
import top.tangyh.lamp.system.vo.result.tenant.DefTenantResultVO;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * 企业
 * </p>
 *
 * @author zuihou
 * @date 2021-09-13
 */
@Repository
@InterceptorIgnore(tenantLine = "true", dynamicTableName = "true")
public interface DefTenantMapper extends SuperMapper<DefTenant> {
    /**
     * 查询用户的可用企业
     *
     * @param userId 用户id
     * @return
     */
    List<DefTenantResultVO> listTenantByUserId(@Param("userId") Long userId);
}
