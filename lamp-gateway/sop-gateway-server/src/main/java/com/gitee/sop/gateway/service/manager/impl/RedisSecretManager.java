package com.gitee.sop.gateway.service.manager.impl;

import com.gitee.sop.gateway.common.CacheKey;
import com.gitee.sop.gateway.dao.entity.IsvKeys;
import com.gitee.sop.support.constant.SopConstants;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 六如
 */
@Slf4j
public class RedisSecretManager extends LocalSecretManagerImpl {

    private static final String KEY_SEC = CacheKey.KEY_SEC;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String getIsvPublicKey(Long isvId) {
        String isvPublicKey = super.getIsvPublicKey(isvId);
        if (StringUtils.hasText(isvPublicKey)) {
            return isvPublicKey;
        }
        try {
            Object value = stringRedisTemplate.opsForHash().get(KEY_SEC, buildHashKey(isvId));
            if (Objects.equals(value, SopConstants.NULL)) {
                return null;
            }
            if (value == null) {
                Map<Long, String> cache = this.refresh(Collections.singletonList(isvId));
                return cache.get(isvId);
            }
            return String.valueOf(value);
        } catch (Exception e) {
            log.error("操作redis失败", e);
            return super.getIsvPublicKey(isvId);
        }
    }

    @Override
    protected void cache(Long isvId, String publicKey) {
        if (publicKey == null) {
            publicKey = SopConstants.NULL;
        }
        super.cache(isvId, publicKey);
        stringRedisTemplate.opsForHash().put(KEY_SEC, buildHashKey(isvId), publicKey);
        log.debug("更新isv秘钥redis缓存, isvId={}", isvId);
    }


    private String buildHashKey(Long isvId) {
        return String.valueOf(isvId);
    }

    @PostConstruct
    @Override
    public void init() {
        log.info("load isvKey to redis");
        List<IsvKeys> isvKeys = this.isvKeysMapper.listAll();
        for (IsvKeys isvKey : isvKeys) {
            this.cache(isvKey.getIsvId(), isvKey.getPublicKeyIsv());
        }
    }

}
