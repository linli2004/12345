package com.gitee.sop.gateway.service.manager.impl;

import com.gitee.sop.gateway.common.ApiInfoDTO;
import com.gitee.sop.gateway.common.CacheKey;
import com.gitee.sop.gateway.dao.entity.IsvInfo;
import com.gitee.sop.gateway.dao.mapper.IsvInfoMapper;
import com.gitee.sop.gateway.util.JsonUtil;
import com.gitee.sop.support.constant.SopConstants;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 六如
 */
@Slf4j
public class RedisIsvApiPermissionManagerImpl extends LocalIsvApiPermissionManagerImpl {

    private static final String CACHE_KEY = CacheKey.KEY_ISV_PERM;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IsvInfoMapper isvInfoMapper;

    @Override
    public boolean doCheck(Long isvId, ApiInfoDTO apiInfoDTO) {
        boolean check = super.doCheck(isvId, apiInfoDTO);
        if (check) {
            return true;
        }
        BoundHashOperations<String, String, String> operations = stringRedisTemplate.boundHashOps(CACHE_KEY);
        String value = operations.get(String.valueOf(isvId));
        if (Objects.equals(value, SopConstants.NULL)) {
            return false;
        }
        List<Long> apiIdList;
        if (value == null) {
            Map<Long, List<Long>> cache = this.refresh(Collections.singletonList(isvId));
            apiIdList = cache.get(isvId);
        } else {
            apiIdList = JsonUtil.parseArray(value, Long.class);
        }
        return apiIdList != null && apiIdList.contains(apiInfoDTO.getId());
    }


    @Override
    protected void cache(Long isvId, List<Long> apiIdList) {
        super.cache(isvId, apiIdList);
        stringRedisTemplate.opsForHash().put(CACHE_KEY, String.valueOf(isvId), JsonUtil.toJSONString(apiIdList));
        log.info("更新isv接口id redis缓存, isvId={}, apiIdList={}", isvId, apiIdList);
    }

    @PostConstruct
    @Override
    public void init() {
        Set<Long> isvIds = isvInfoMapper.listAll()
                .stream()
                .map(IsvInfo::getId)
                .collect(Collectors.toSet());
        this.refresh(isvIds);
    }


}
