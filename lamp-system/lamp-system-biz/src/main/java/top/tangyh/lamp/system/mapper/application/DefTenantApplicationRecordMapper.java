package top.tangyh.lamp.system.mapper.application;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.springframework.stereotype.Repository;
import top.tangyh.basic.base.mapper.SuperMapper;
import top.tangyh.lamp.system.entity.application.DefTenantApplicationRecord;

/**
 * <p>
 * Mapper 接口
 * 租户应用授权记录
 * </p>
 *
 * @author zuihou
 * @date 2021-09-15
 */
@Repository
@InterceptorIgnore(tenantLine = "true", dynamicTableName = "true")
public interface DefTenantApplicationRecordMapper extends SuperMapper<DefTenantApplicationRecord> {

}
