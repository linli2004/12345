package top.tangyh.lamp.system.service.tenant.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperCacheServiceImpl;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.model.cache.CacheKey;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.common.cache.tenant.tenant.DefUserTenantCacheKeyBuilder;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.system.entity.tenant.DefUserTenantRel;
import top.tangyh.lamp.system.manager.tenant.DefUserTenantRelManager;
import top.tangyh.lamp.system.service.tenant.DefUserTenantRelService;
import top.tangyh.lamp.system.vo.result.tenant.DefUserTenantRelResultVO;

import java.util.List;

/**
 * <p>
 * 业务实现类
 * 员工
 * </p>
 *
 * @author zuihou
 * @date 2021-10-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@DS(DsConstant.DEFAULTS)
public class DefUserTenantRelServiceImpl extends SuperCacheServiceImpl<DefUserTenantRelManager, Long, DefUserTenantRel> implements DefUserTenantRelService {
    @Override
    public List<DefUserTenantRelResultVO> listEmployeeByUserId(Long userId) {
        return superManager.listEmployeeByUserId(userId);
    }

    @Override
    public DefUserTenantRel getEmployeeByTenantAndUser(Long tenantId, Long userId) {
        ArgumentAssert.notNull(tenantId, "租户id为空");
        ArgumentAssert.notNull(userId, "用户id为空");
        return superManager.getOne(Wraps.<DefUserTenantRel>lbQ().eq(DefUserTenantRel::getTenantId, tenantId)
                .eq(DefUserTenantRel::getUserId, userId));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDefaultTenant(Long tenantId, Long userId) {
        ArgumentAssert.notNull(userId, "用户id不能为空");
        ArgumentAssert.notNull(tenantId, "企业id不能为空");

        // 演示环境专用标识，用于WriteInterceptor拦截器判断演示环境需要禁止用户执行sql，若您无需搭建演示环境，可以删除下面一行代码
        if (Long.valueOf(1).equals(ContextUtil.getTenantId())) {
            ContextUtil.setStop();
        }

        List<DefUserTenantRel> list = superManager.list(Wraps.<DefUserTenantRel>lbQ().eq(DefUserTenantRel::getUserId, userId));
        CacheKey[] keyList = list.stream().map(item -> DefUserTenantCacheKeyBuilder.build(item.getId())).toArray(CacheKey[]::new);
        cacheOps.del(keyList);

        superManager.update(Wraps.<DefUserTenantRel>lbU()
                .set(DefUserTenantRel::getIsDefault, false)
                .eq(DefUserTenantRel::getUserId, userId));

        return superManager.update(Wraps.<DefUserTenantRel>lbU()
                .set(DefUserTenantRel::getIsDefault, true)
                .eq(DefUserTenantRel::getUserId, userId)
                .eq(DefUserTenantRel::getTenantId, tenantId)
        );
    }
}
