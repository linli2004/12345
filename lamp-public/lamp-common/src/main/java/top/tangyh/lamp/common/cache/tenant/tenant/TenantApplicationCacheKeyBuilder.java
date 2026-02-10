package top.tangyh.lamp.common.cache.tenant.tenant;

import top.tangyh.basic.base.entity.SuperEntity;
import top.tangyh.basic.model.cache.CacheKey;
import top.tangyh.basic.model.cache.CacheKeyBuilder;
import top.tangyh.basic.utils.StrPool;
import top.tangyh.lamp.common.cache.CacheKeyModular;
import top.tangyh.lamp.common.cache.CacheKeyTable;

import java.time.Duration;

/**
 * 租户拥有的应用
 * <p>
 * #def_tenant_application_rel
 *
 * @author zuihou
 * @date 2020/9/20 6:45 下午
 */
public class TenantApplicationCacheKeyBuilder implements CacheKeyBuilder {
    public static CacheKey builder(Long tenantId) {
        return new TenantApplicationCacheKeyBuilder().key(tenantId);
    }



    @Override
    public String getTenant() {
        return StrPool.EMPTY;
    }

    @Override
    public String getModular() {
        return CacheKeyModular.SYSTEM;
    }

    @Override
    public String getTable() {
        return CacheKeyTable.System.TENANT_APPLICATION;
    }

    @Override
    public String getField() {
        return SuperEntity.ID_FIELD;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.number;
    }

    @Override
    public Duration getExpire() {
        return Duration.ofHours(24);
    }
}
