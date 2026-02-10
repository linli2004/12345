package top.tangyh.lamp.sop.manager.impl;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.lamp.sop.entity.SopIsvInfo;
import top.tangyh.lamp.sop.manager.SopIsvInfoManager;
import top.tangyh.lamp.sop.mapper.SopIsvInfoMapper;

/**
 * <p>
 * 通用业务实现类
 * isv信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SopIsvInfoManagerImpl extends SuperManagerImpl<SopIsvInfoMapper, SopIsvInfo> implements SopIsvInfoManager {
    @Override
    public SopIsvInfo getIsvByAppId(String appId) {
        if (StrUtil.isEmpty(appId)) {
            return null;
        }
        return getOne(Wraps.<SopIsvInfo>lbQ().eq(SopIsvInfo::getAppId, appId), false);
    }

    @Override
    public String getPrivatePlatformKey(String appId) {
        return baseMapper.getPrivatePlatformKey(appId);
    }
}


