package top.tangyh.lamp.test.controller.gateway;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.basic.context.ContextUtil;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/requestWhitelist")
@Tag(name = "请求放行")
public class RequestWhitelistController {

    private static void logMap() {
        Map<String, String> localMap = ContextUtil.getLocalMap();
        localMap.forEach((k, v) -> {
            log.info("k={}, v={}", k, v);
        });
    }

    @GetMapping("/anyone/test")
    public R<Object> anyone(@RequestParam(required = false) Long id) throws InterruptedException {
        logMap();
        return R.success(id);
    }

    @GetMapping("/anyUser/test")
    public R<Object> anyUser(@RequestParam(required = false) Long id) throws InterruptedException {
        logMap();
        return R.success(id);
    }

    @GetMapping("/anyTenant/test")
    public R<Object> anyTenant(@RequestParam(required = false) Long id) throws InterruptedException {
        logMap();
        return R.success(id);
    }

    @GetMapping("/aaaa")
    public R<Object> aaaa(@RequestParam(required = false) Long id) throws InterruptedException {
        logMap();
        return R.success(id);
    }

}
