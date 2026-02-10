package com.gitee.sop.gateway.service.manager.impl;

import com.gitee.sop.gateway.common.ApiInfoDTO;
import com.gitee.sop.gateway.common.enums.YesOrNoEnum;
import com.gitee.sop.gateway.dao.entity.ApiInfo;
import com.gitee.sop.gateway.dao.entity.PermGroupPermission;
import com.gitee.sop.gateway.dao.entity.PermIsvGroup;
import com.gitee.sop.gateway.dao.mapper.ApiInfoMapper;
import com.gitee.sop.gateway.dao.mapper.PermGroupPermissionMapper;
import com.gitee.sop.gateway.dao.mapper.PermIsvGroupMapper;
import com.gitee.sop.gateway.service.manager.IsvApiPermissionManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 缓存ISV接口权限
 *
 * @author 六如
 */
@Slf4j
public class LocalIsvApiPermissionManagerImpl implements IsvApiPermissionManager {

    // key:isvId, value: List<apiName+apiVersion>
    private static final Map<Long, List<Long>> CACHE = new HashMap<>();

    @Resource
    private PermGroupPermissionMapper permGroupPermissionMapper;
    @Resource
    private PermIsvGroupMapper permIsvGroupMapper;
    @Resource
    private ApiInfoMapper apiInfoMapper;

    @Override
    public boolean hasPermission(Long isvId, ApiInfoDTO apiInfoDTO) {
        // 通用接口都可以访问
        if (Objects.equals(apiInfoDTO.getIsPermission(), YesOrNoEnum.NO.getValue())) {
            return true;
        }
        return doCheck(isvId, apiInfoDTO);
    }

    public boolean doCheck(Long isvId, ApiInfoDTO apiInfoDTO) {
        List<Long> apiNameVerionList = CACHE.computeIfAbsent(isvId, k -> this.listApiId(isvId));
        if (CollectionUtils.isEmpty(apiNameVerionList)) {
            return false;
        }
        return apiNameVerionList.contains(apiInfoDTO.getId());
    }

    @Override
    public Map<Long, List<Long>> refresh(Collection<Long> isvIds) {
        log.info("刷新isv接口权限, isvIds={}", isvIds);
        if (CollectionUtils.isEmpty(isvIds)) {
            return Collections.emptyMap();
        }
        Map<Long, List<Long>> map = new HashMap<>(isvIds.size() * 2);
        for (Long isvId : isvIds) {
            List<Long> apiIdList = this.listApiId(isvId);
            map.put(isvId, apiIdList);
            // 缓存
            cache(isvId, apiIdList);
        }
        return map;
    }

    protected void cache(Long isvId, List<Long> apiIdList) {
        CACHE.put(isvId, apiIdList);
        log.info("更新isv接口id本地缓存, isvId={}, apiIdList={}", isvId, apiIdList);
    }

    protected List<Long> listApiId(Long isvId) {
        List<Long> groupIds = permIsvGroupMapper.query()
                .eq(PermIsvGroup::getIsvId, isvId)
                .listUniqueValue(PermIsvGroup::getGroupId);
        if (groupIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> apiIdList = permGroupPermissionMapper.query()
                .in(PermGroupPermission::getGroupId, groupIds)
                .listUniqueValue(PermGroupPermission::getApiId);
        if (apiIdList.isEmpty()) {
            return Collections.emptyList();
        }
        return apiInfoMapper.query()
                .select(ApiInfo::getApiName, ApiInfo::getApiVersion)
                .in(ApiInfo::getId, apiIdList)
                .listUniqueValue(ApiInfo::getId);
    }


}
