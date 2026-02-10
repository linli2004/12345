package top.tangyh.lamp.sop.manager;

import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.basic.utils.SpringUtils;
import top.tangyh.lamp.sop.entity.SopIsvInfo;
import top.tangyh.lamp.sop.event.ChangeIsvInfoEvent;

import java.util.Collection;
import java.util.Collections;

/**
 * <p>
 * 通用业务接口
 * isv信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
public interface SopIsvInfoManager extends SuperManager<SopIsvInfo> {
    default void sendChangeEvent(Long isvId) {
        SpringUtils.publishEvent(new ChangeIsvInfoEvent(Collections.singletonList(isvId)));
    }

    default void sendChangeEvent(Collection<Long> isvIds) {
        SpringUtils.publishEvent(new ChangeIsvInfoEvent(isvIds));
    }

    SopIsvInfo getIsvByAppId(String appId);

    /**
     * 获取平台密钥
     *
     * @param appId 应用唯一标识
     * @return 密钥
     */
    String getPrivatePlatformKey(String appId);
}


