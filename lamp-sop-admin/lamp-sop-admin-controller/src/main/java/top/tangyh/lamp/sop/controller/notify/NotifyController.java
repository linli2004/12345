package top.tangyh.lamp.sop.controller.notify;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.basic.utils.SpringUtils;
import top.tangyh.lamp.sop.dto.NotifyRequest;
import top.tangyh.lamp.sop.service.NotifyService;

/**
 * <p>
 * 前端控制器
 * 回调接口，提供给业务系统进行回调
 * </p>
 *
 * @author zuihou
 * @date 2025-12-17 15:38:07
 * @create [2025-12-17 15:38:07] [zuihou] [代码生成器生成]
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/notify")
@Tag(name = "回调接口")
public class NotifyController {
    @Resource
    private NotifyService notifyService;

    /**
     * 回调
     * SOP服务端在处理某些业务数据后，可以调用此回调接口通知业务系统
     *
     * @param request 参数
     * @return 返回结果 回调ID
     */
    @PostMapping(value = "/exec")
    private R<Long> notify(@Validated @RequestBody NotifyRequest request) {
        return SpringUtils.getBean(NotifyService.class).notify(request);
    }
}


