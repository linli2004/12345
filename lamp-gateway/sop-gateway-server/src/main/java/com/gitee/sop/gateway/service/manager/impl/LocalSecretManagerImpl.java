package com.gitee.sop.gateway.service.manager.impl;

import com.gitee.sop.gateway.dao.entity.IsvKeys;
import com.gitee.sop.gateway.dao.mapper.IsvKeysMapper;
import com.gitee.sop.gateway.service.manager.SecretManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 六如
 */
@Slf4j
public class LocalSecretManagerImpl implements SecretManager {

    private static final Map<Long, Optional<String>> CACHE = new ConcurrentHashMap<>();

    @Resource
    protected IsvKeysMapper isvKeysMapper;

    @Override
    public String getIsvPublicKey(Long isvId) {
        return CACHE.computeIfAbsent(isvId, k -> {
                    String publicKey = doGetPublicKey(isvId);
                    return Optional.ofNullable(publicKey);
                })
                .orElse(null);
    }

    @Override
    public Map<Long, String> refresh(Collection<Long> isvIds) {
        log.info("刷新isv秘钥, isvId={}", isvIds);
        if (CollectionUtils.isEmpty(isvIds)) {
            return Collections.emptyMap();
        }
        Map<Long, String> map = new HashMap<>(isvIds.size() * 2);
        for (Long isvId : isvIds) {
            String publicKey = doGetPublicKey(isvId);
            map.put(isvId, publicKey);

            this.cache(isvId, publicKey);
        }
        return map;
    }

    protected void cache(Long isvId, String publicKey) {
        CACHE.put(isvId, Optional.ofNullable(publicKey));
        log.info("更新isv秘钥本地缓存, isvId={}", isvId);
    }

    protected String doGetPublicKey(Long isvId) {
        return isvKeysMapper.query()
                .eq(IsvKeys::getIsvId, isvId)
                .getValue(IsvKeys::getPublicKeyIsv);
    }

}
