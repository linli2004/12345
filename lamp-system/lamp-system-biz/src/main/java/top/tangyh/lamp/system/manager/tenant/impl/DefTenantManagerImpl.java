package top.tangyh.lamp.system.manager.tenant.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperCacheManagerImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.model.cache.CacheKeyBuilder;
import top.tangyh.basic.utils.CollHelper;
import top.tangyh.lamp.common.cache.tenant.tenant.TenantCacheKeyBuilder;
import top.tangyh.lamp.model.constant.EchoApi;
import top.tangyh.lamp.model.enumeration.system.DefTenantStatusEnum;
import top.tangyh.lamp.system.entity.tenant.DefTenant;
import top.tangyh.lamp.system.manager.tenant.DefTenantManager;
import top.tangyh.lamp.system.mapper.tenant.DefTenantMapper;
import top.tangyh.lamp.system.vo.result.tenant.DefTenantResultVO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 应用管理
 *
 * @author tangyh
 * @version v1.0
 * @date 2021/9/29 1:26 下午
 * @create [2021/9/29 1:26 下午 ] [tangyh] [初始创建]
 */
@RequiredArgsConstructor
@Service(EchoApi.DEF_TENANT_SERVICE_IMPL_CLASS)
public class DefTenantManagerImpl extends SuperCacheManagerImpl<DefTenantMapper, DefTenant> implements DefTenantManager {
    @Override
    protected CacheKeyBuilder cacheKeyBuilder() {
        return new TenantCacheKeyBuilder();
    }

    @Override
    public Map<Serializable, Object> findByIds(Set<Serializable> ids) {
        return CollHelper.uniqueIndex(find(ids), DefTenant::getId, DefTenant::getName);
    }

    public List<DefTenant> find(Set<Serializable> ids) {
        // 强转， 防止数据库隐式转换，  若你的id 是string类型，请勿强转
        return findByIds(ids, null).stream().filter(Objects::nonNull).toList();
    }


    @Override
    public List<DefTenant> listNormal() {
        return list(Wraps.<DefTenant>lbQ().eq(DefTenant::getStatus, DefTenantStatusEnum.NORMAL.getCode()));
    }

    @Override
    public List<DefTenantResultVO> listTenantByUserId(Long userId) {
        return baseMapper.listTenantByUserId(userId);
    }
}
