package top.tangyh.lamp.sop.manager;

import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.basic.utils.SpringUtils;
import top.tangyh.lamp.sop.entity.SopIsvKeys;
import top.tangyh.lamp.sop.event.ChangeIsvKeyEvent;

import java.util.Collections;

/**
 * <p>
 * 通用业务接口
 * ISV秘钥管理
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
public interface SopIsvKeysManager extends SuperManager<SopIsvKeys> {

    default void sendChangeEvent(Long isvId) {
        SpringUtils.publishEvent(new ChangeIsvKeyEvent(Collections.singletonList(isvId)));
    }

}


