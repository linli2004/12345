package com.gitee.sop.gateway.service.manager.impl;

import com.gitee.sop.gateway.dao.entity.IsvInfo;
import com.gitee.sop.gateway.dao.mapper.IsvInfoMapper;
import com.gitee.sop.gateway.service.manager.IsvManager;
import com.gitee.sop.gateway.service.manager.dto.IsvDTO;
import com.gitee.sop.gateway.util.CopyUtil;
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
public class LocalIsvManagerImpl implements IsvManager {

    private static final Map<String, Optional<IsvDTO>> CACHE = new ConcurrentHashMap<>();

    @Resource
    protected IsvInfoMapper isvInfoMapper;

    @Override
    public IsvDTO getIsv(String appId) {
        return CACHE.computeIfAbsent(appId, k -> {
            IsvInfo isvInfo = isvInfoMapper.getByAppId(appId);
            return Optional.ofNullable(CopyUtil.copyBean(isvInfo, IsvDTO::new));
        }).orElse(null);
    }

    @Override
    public Map<String, IsvDTO> refresh(Collection<String> appIds) {
        log.info("刷新isv, appId={}", appIds);
        if (CollectionUtils.isEmpty(appIds)) {
            return Collections.emptyMap();
        }
        Map<String, IsvDTO> map = new HashMap<>(appIds.size() * 2);
        for (String appId : appIds) {
            IsvInfo isvInfo = isvInfoMapper.getByAppId(appId);
            if (isvInfo != null) {
                IsvDTO isvDTO = CopyUtil.copyBean(isvInfo, IsvDTO::new);
                map.put(appId, isvDTO);
                cache(appId, isvDTO);
            } else {
                clear(appId);
            }
        }
        return map;
    }

    protected void cache(String appId, IsvDTO isvDTO) {
        CACHE.put(appId, Optional.ofNullable(isvDTO));
        log.info("更新isv本地缓存, isvDTO={}", isvDTO);
    }

    protected void clear(String appId) {
        CACHE.remove(appId);
        log.info("清理isv本地缓存, appId={}", appId);
    }

}
