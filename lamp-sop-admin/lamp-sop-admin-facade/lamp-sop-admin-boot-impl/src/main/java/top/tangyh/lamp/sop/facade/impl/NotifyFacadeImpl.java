package top.tangyh.lamp.sop.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.R;
import top.tangyh.lamp.sop.dto.NotifyRequest;
import top.tangyh.lamp.sop.facade.NotifyFacade;
import top.tangyh.lamp.sop.service.NotifyService;

/**
 *
 * @author tangyh
 * @since 2025/12/18 00:10
 */
@Service
@RequiredArgsConstructor
public class NotifyFacadeImpl implements NotifyFacade {
    private final NotifyService notifyService;

    @Override
    public R<Long> notify(NotifyRequest request) {
        return notifyService.notify(request);
    }
}
