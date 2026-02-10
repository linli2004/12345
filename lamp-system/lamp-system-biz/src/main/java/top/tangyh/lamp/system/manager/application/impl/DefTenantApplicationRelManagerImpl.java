package top.tangyh.lamp.system.manager.application.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.basic.cache.redis2.CacheResult;
import top.tangyh.basic.cache.repository.CacheOps;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.model.cache.CacheKey;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.common.cache.tenant.tenant.TenantApplicationCacheKeyBuilder;
import top.tangyh.lamp.system.entity.application.DefApplication;
import top.tangyh.lamp.system.entity.application.DefResource;
import top.tangyh.lamp.system.entity.application.DefTenantApplicationRel;
import top.tangyh.lamp.system.entity.application.DefTenantResourceRel;
import top.tangyh.lamp.system.entity.tenant.DefUserTenantRel;
import top.tangyh.lamp.system.manager.application.DefApplicationManager;
import top.tangyh.lamp.system.manager.application.DefResourceManager;
import top.tangyh.lamp.system.manager.application.DefTenantApplicationRelManager;
import top.tangyh.lamp.system.manager.application.DefTenantResourceRelManager;
import top.tangyh.lamp.system.manager.tenant.DefUserTenantRelManager;
import top.tangyh.lamp.system.mapper.application.DefTenantApplicationRelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用管理
 *
 * @author tangyh
 * @version v1.0
 * @date 2021/9/29 1:26 下午
 * @create [2021/9/29 1:26 下午 ] [tangyh] [初始创建]
 */
@RequiredArgsConstructor
@Service
public class DefTenantApplicationRelManagerImpl extends SuperManagerImpl<DefTenantApplicationRelMapper, DefTenantApplicationRel> implements DefTenantApplicationRelManager {
    private final DefApplicationManager defApplicationManager;
    private final DefResourceManager defResourceManager;
    private final DefTenantResourceRelManager defTenantResourceRelManager;
    private final DefUserTenantRelManager defUserTenantRelManager;
    private final CacheOps cacheOps;

    @Override
    public void grantGeneralApplication(Long tenantId) {
        List<DefApplication> list = defApplicationManager.findGeneral();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<Long> applicationIds = new ArrayList<>();
        List<DefTenantApplicationRel> tarList = list.stream().map(application -> {
            DefTenantApplicationRel tar = new DefTenantApplicationRel();
            tar.setTenantId(tenantId);
            tar.setApplicationId(application.getId());
            applicationIds.add(application.getId());
            return tar;
        }).toList();
        saveBatch(tarList);

        List<DefResource> resourceList = defResourceManager.findByApplicationId(applicationIds);
        if (CollUtil.isEmpty(resourceList)) {
            return;
        }
        List<DefTenantResourceRel> trrList = resourceList.stream().map(resource -> {
            DefTenantResourceRel trr = new DefTenantResourceRel();
            trr.setTenantId(tenantId);
            trr.setApplicationId(resource.getApplicationId());
            trr.setResourceId(resource.getId());
            return trr;
        }).toList();
        defTenantResourceRelManager.saveBatch(trrList);
    }


    @Override
    public List<Long> findApplicationByEmployeeId(Long employeeId) {
        DefUserTenantRel employee = defUserTenantRelManager.getByIdCache(employeeId);
        ArgumentAssert.notNull(employee, "用户不存在");
        Long tenantId = employee.getTenantId();
        CacheKey key = TenantApplicationCacheKeyBuilder.builder(tenantId);
        CacheResult<List<Long>> applicationIds = cacheOps.get(key, k -> listObjs(
                Wraps.<DefTenantApplicationRel>lbQ().select(DefTenantApplicationRel::getApplicationId)
                        .eq(DefTenantApplicationRel::getTenantId, tenantId)
                        .and(w ->
                                w.gt(DefTenantApplicationRel::getExpirationTime, LocalDateTime.now()).or().isNull(DefTenantApplicationRel::getExpirationTime)
                        ), Convert::toLong));
        return applicationIds.asList();
    }

    @Override
    public void deleteByTenantId(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        remove(Wraps.<DefTenantApplicationRel>lbQ().in(DefTenantApplicationRel::getTenantId, ids));

        cacheOps.del(ids.stream().map(TenantApplicationCacheKeyBuilder::builder).toList());
    }
}
