package com.gitee.sop.gateway.service.manager.impl;

import com.gitee.sop.gateway.common.ApiInfoDTO;
import com.gitee.sop.gateway.dao.entity.ApiInfo;
import com.gitee.sop.gateway.dao.mapper.ApiInfoMapper;
import com.gitee.sop.gateway.service.manager.ApiManager;
import com.gitee.sop.gateway.util.CopyUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 本地存储接口信息.
 *
 * @author 六如
 */
@Slf4j
public class LocalApiManagerImpl implements ApiManager {

    private static final Map<String, Optional<ApiInfoDTO>> CACHE = new ConcurrentHashMap<>();

    @Resource
    protected ApiInfoMapper apiInfoMapper;

    @Override
    public void save(ApiInfoDTO apiInfoDTO) {
        String key = apiInfoDTO.buildApiNameVersion();
        CACHE.put(key, Optional.of(apiInfoDTO));
    }


    @Override
    public ApiInfoDTO get(String apiName, String apiVersion) {
        String key = apiName + apiVersion;
        return CACHE.computeIfAbsent(key, k -> {
            ApiInfo apiInfo = apiInfoMapper.getByNameVersion(apiName, apiVersion);
            return Optional.ofNullable(CopyUtil.copyBean(apiInfo, ApiInfoDTO::new));
        }).orElse(null);
    }

    @Override
    public Map<Long, ApiInfoDTO> refresh(Collection<Long> id) {
        log.info("刷新api信息, id={}", id);
        Map<Long, ApiInfo> apiIdMap = apiInfoMapper.query()
                .in(ApiInfo::getId, id)
                .map(ApiInfo::getId, Function.identity());

        apiIdMap.values().forEach(this::cache);
        return Collections.emptyMap();
    }

    protected ApiInfoDTO cache(ApiInfo apiInfo) {
        ApiInfoDTO apiInfoDTO = CopyUtil.copyBean(apiInfo, ApiInfoDTO::new);
        String key = apiInfoDTO.buildApiNameVersion();
        CACHE.put(key, Optional.of(apiInfoDTO));
        log.info("更新接口本地缓存, apiInfoDTO={}", apiInfoDTO);
        return apiInfoDTO;
    }

}
