package top.tangyh.lamp.sop.service;

import top.tangyh.basic.base.R;
import top.tangyh.lamp.sop.dto.NotifyRequest;

import java.time.LocalDateTime;

/**
 *
 * @author tangyh
 * @since 2025/12/17 15:40
 */
public interface NotifyService {
    /**
     * 回调重试
     * @param now 当前时间
     */
    void retry(LocalDateTime now);

    /**
     * 第一次回调
     *
     * @param request 回调内容
     * @return 返回回调id
     */
    R<Long> notify(NotifyRequest request);
    /**
     * 回调立即发送
     *
     * @param notifyId notifyId
     * @return 返回结果
     */
    Long notifyImmediately(Long notifyId);
}
