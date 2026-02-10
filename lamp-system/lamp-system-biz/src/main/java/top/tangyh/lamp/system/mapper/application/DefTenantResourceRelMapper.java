package top.tangyh.lamp.system.mapper.application;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.springframework.stereotype.Repository;
import top.tangyh.basic.base.mapper.SuperMapper;
import top.tangyh.lamp.system.entity.application.DefTenantResourceRel;

/**
 * <p>
 * Mapper 接口
 * 租户的资源
 * </p>
 *
 * @author zuihou
 * @date 2021-09-13
 */
@Repository
@InterceptorIgnore(tenantLine = "true", dynamicTableName = "true")
public interface DefTenantResourceRelMapper extends SuperMapper<DefTenantResourceRel> {

}
