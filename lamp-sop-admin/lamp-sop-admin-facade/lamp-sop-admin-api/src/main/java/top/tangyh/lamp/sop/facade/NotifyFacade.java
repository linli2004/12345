package top.tangyh.lamp.sop.facade;

import top.tangyh.basic.base.R;
import top.tangyh.lamp.sop.dto.NotifyRequest;

/**
 *
 * @author tangyh
 * @since 2025/12/18 00:09
 */
public interface NotifyFacade {
    /**
     * 回调
     *
     * @param request 参数
     * @return 返回结果 回调ID
     */
    R<Long> notify(NotifyRequest request);
}
