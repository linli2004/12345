package com.gitee.sop.gateway.service.manager.impl;

import com.gitee.sop.gateway.common.ApiInfoDTO;
import com.gitee.sop.gateway.common.CacheKey;
import com.gitee.sop.gateway.dao.entity.ApiInfo;
import com.gitee.sop.gateway.util.CopyUtil;
import com.gitee.sop.gateway.util.JsonUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * redis存储接口信息
 *
 * @author 六如
 */
@Slf4j
public class RedisApiManagerImpl extends LocalApiManagerImpl {

    private static final String KEY_API = CacheKey.KEY_API;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void save(ApiInfoDTO apiInfoDTO) {
        String key = apiInfoDTO.buildApiNameVersion();
        stringRedisTemplate.opsForHash().put(KEY_API, key, JsonUtil.toJSONString(apiInfoDTO));
    }

    protected ApiInfoDTO cache(ApiInfo apiInfo) {
        super.cache(apiInfo);
        String key = apiInfo.getApiName() + apiInfo.getApiVersion();
        ApiInfoDTO apiInfoDTO = CopyUtil.copyBean(apiInfo, ApiInfoDTO::new);
        stringRedisTemplate.opsForHash().put(KEY_API, key, JsonUtil.toJSONString(apiInfoDTO));
        log.info("更新接口redis缓存, apiInfoDTO={}", apiInfoDTO);
        return apiInfoDTO;
    }

    @Override
    public ApiInfoDTO get(String apiName, String apiVersion) {
        ApiInfoDTO apiInfoDTO = super.get(apiName, apiVersion);
        if (apiInfoDTO != null) {
            return apiInfoDTO;
        }
        String key = apiName + apiVersion;
        try {
            BoundHashOperations<String, String, String> operations = stringRedisTemplate.boundHashOps(KEY_API);
            String value = operations.get(key);
            if (value == null) {
                // 从数据库中读取
                ApiInfo apiInfo = apiInfoMapper.getByNameVersion(apiName, apiVersion);
                if (apiInfo == null) {
                    operations.put(key, "");
                    return null;
                }
                return this.cache(apiInfo);
            }
            if (ObjectUtils.isEmpty(value)) {
                return null;
            }
            return JsonUtil.parseObject(value, ApiInfoDTO.class);
        } catch (Exception e) {
            log.error("redis访问失败", e);
            return super.get(apiName, apiVersion);
        }
    }

    @PostConstruct
    public void init() {
        log.info("load apiInfo to redis");
        List<ApiInfo> apiInfos = this.apiInfoMapper.listAll();
        for (ApiInfo apiInfo : apiInfos) {
            this.cache(apiInfo);
        }
    }
}
