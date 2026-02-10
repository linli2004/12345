package top.tangyh.lamp.system.manager.tenant.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperCacheManagerImpl;
import top.tangyh.basic.model.cache.CacheKeyBuilder;
import top.tangyh.lamp.common.cache.tenant.tenant.DefUserTenantCacheKeyBuilder;
import top.tangyh.lamp.system.entity.tenant.DefUserTenantRel;
import top.tangyh.lamp.system.manager.tenant.DefUserTenantRelManager;
import top.tangyh.lamp.system.mapper.tenant.DefUserTenantRelMapper;
import top.tangyh.lamp.system.vo.result.tenant.DefUserTenantRelResultVO;

import java.util.List;

/**
 * <p>
 * 通用业务实现类
 * 员工
 * </p>
 *
 * @author zuihou
 * @date 2021-10-27
 * @create [2021-10-27] [zuihou] [代码生成器生成]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefUserTenantRelManagerImpl extends SuperCacheManagerImpl<DefUserTenantRelMapper, DefUserTenantRel> implements DefUserTenantRelManager {
    @Override
    protected CacheKeyBuilder cacheKeyBuilder() {
        return new DefUserTenantCacheKeyBuilder();
    }


    @Override
    public List<DefUserTenantRelResultVO> listEmployeeByUserId(Long userId) {
        return baseMapper.listEmployeeByUserId(userId);
    }
}
