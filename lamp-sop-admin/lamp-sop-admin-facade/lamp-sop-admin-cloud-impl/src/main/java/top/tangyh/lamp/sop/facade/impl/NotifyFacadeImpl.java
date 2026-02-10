package top.tangyh.lamp.sop.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.R;
import top.tangyh.lamp.sop.api.NotifyApi;
import top.tangyh.lamp.sop.dto.NotifyRequest;
import top.tangyh.lamp.sop.facade.NotifyFacade;

/**
 *
 * @author tangyh
 * @since 2025/12/18 00:10
 */
@Service
public class NotifyFacadeImpl implements NotifyFacade {
    @Autowired
    @Lazy
    private NotifyApi notifyApi;

    @Override
    public R<Long> notify(NotifyRequest request) {
        return notifyApi.notify(request);
    }
}
