package top.tangyh.lamp.system.manager.application.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.manager.impl.SuperCacheManagerImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.basic.model.cache.CacheKey;
import top.tangyh.basic.model.cache.CacheKeyBuilder;
import top.tangyh.lamp.common.cache.tenant.application.ResourceApiCacheKeyBuilder;
import top.tangyh.lamp.model.vo.result.ResourceApiVO;
import top.tangyh.lamp.system.entity.application.DefResourceApi;
import top.tangyh.lamp.system.manager.application.DefResourceApiManager;
import top.tangyh.lamp.system.mapper.application.DefResourceApiMapper;

import java.util.Collections;
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
@Slf4j
public class DefResourceApiManagerImpl extends SuperCacheManagerImpl<DefResourceApiMapper, DefResourceApi> implements DefResourceApiManager {
    @Override
    protected CacheKeyBuilder cacheKeyBuilder() {
        return new ResourceApiCacheKeyBuilder();
    }

    @Override
    public List<ResourceApiVO> findAllApi() {
        return baseMapper.findAllApi();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByResourceId(List<Long> resourceIdList) {
        LbQueryWrap<DefResourceApi> wrap = Wraps.<DefResourceApi>lbQ().select(DefResourceApi::getId).in(DefResourceApi::getResourceId, resourceIdList);
        List<Long> apiIds = listObjs(wrap, Convert::toLong);
        remove(wrap);

        CacheKey[] keys = apiIds.stream().map(ResourceApiCacheKeyBuilder::builder).toArray(CacheKey[]::new);
        cacheOps.del(keys);

    }

    @Override
    public List<DefResourceApi> findByResourceId(Long resourceId) {
        if (resourceId == null) {
            return Collections.emptyList();
        }
        return list(Wraps.<DefResourceApi>lbQ().eq(DefResourceApi::getResourceId, resourceId));
    }

    @Override
    public List<DefResourceApi> findByResourceId(List<Long> resourceIds) {
        if (CollUtil.isEmpty(resourceIds)) {
            return Collections.emptyList();
        }
        return list(Wraps.<DefResourceApi>lbQ().in(DefResourceApi::getResourceId, resourceIds));
    }
}
