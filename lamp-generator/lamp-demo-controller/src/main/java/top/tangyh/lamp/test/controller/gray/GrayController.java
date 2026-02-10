package top.tangyh.lamp.test.controller.gray;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/gray")
@Tag(name = "灰度")
public class GrayController {

    //    private final GrayApi grayApi;
    @Value("${server.port}")
    private String port;

//    @GetMapping("/test1")
//    public R<Object> test1(@RequestParam(required = false) Long id) {
//        log.info("port={}, id={}", port, id);
//        logMap();
//        return grayApi.test2(id);
//    }

    private static void logMap() {
        Map<String, String> localMap = ContextUtil.getLocalMap();
        localMap.forEach((k, v) -> {
            log.info("k={}, v={}", k, v);
        });
    }

    @GetMapping("/test2")
    public R<Object> test2(@RequestParam(required = false) Long id) {
        log.info("port={}, id={}", port, id);
        logMap();
        return R.success(port);
    }


}
