package com.gitee.sop.gateway.service.manager.impl;

import com.gitee.sop.gateway.common.CacheKey;
import com.gitee.sop.gateway.dao.entity.IsvInfo;
import com.gitee.sop.gateway.service.manager.dto.IsvDTO;
import com.gitee.sop.gateway.util.CopyUtil;
import com.gitee.sop.gateway.util.JsonUtil;
import com.gitee.sop.support.constant.SopConstants;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 六如
 */
@Slf4j
public class RedisIsvManagerImpl extends LocalIsvManagerImpl {

    private static final String KEY_ISV = CacheKey.KEY_ISV;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public IsvDTO getIsv(String appId) {
        IsvDTO isv = super.getIsv(appId);
        if (isv != null) {
            return isv;
        }
        try {
            Object value = stringRedisTemplate.opsForHash().get(KEY_ISV, appId);
            if (Objects.equals(value, SopConstants.NULL)) {
                return null;
            }
            if (value == null) {
                Map<String, IsvDTO> cache = this.refresh(Collections.singletonList(appId));
                return cache.get(appId);
            }
            return JsonUtil.parseObject(String.valueOf(value), IsvDTO.class);
        } catch (Exception e) {
            log.error("操作redis失败", e);
            return super.getIsv(appId);
        }
    }

    @Override
    protected void cache(String appId, IsvDTO isvDTO) {
        super.cache(appId, isvDTO);
        stringRedisTemplate.opsForHash().put(KEY_ISV, appId, JsonUtil.toJSONString(isvDTO));
        log.debug("更新isv redis缓存, isvDTO={}", isvDTO);
    }

    @Override
    protected void clear(String appId) {
        super.clear(appId);
        stringRedisTemplate.opsForHash().delete(KEY_ISV, appId);
        log.info("清理isv redis缓存, appId={}", appId);
    }


    @PostConstruct
    @Override
    public void init() {
        log.info("load isvInfo to redis");
        List<IsvInfo> isvInfos = this.isvInfoMapper.listAll();
        for (IsvInfo isvInfo : isvInfos) {
            this.cache(isvInfo.getAppId(), CopyUtil.copyBean(isvInfo, IsvDTO::new));
        }
    }

}
