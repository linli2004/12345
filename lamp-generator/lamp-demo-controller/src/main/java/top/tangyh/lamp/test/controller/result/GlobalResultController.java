package top.tangyh.lamp.test.controller.result;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.response.IgnoreResponseBodyAdvice;
import top.tangyh.basic.base.R;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/GlobalResultController")
@Tag(name = "全局返回")
public class GlobalResultController {

    @GetMapping("/test1")
    public R<Object> test1(@RequestParam(required = false) Long id) throws InterruptedException {

        return R.success(id);
    }

    @GetMapping("/test2")
    public Boolean test2(@RequestParam(required = false) Long id) throws InterruptedException {

        return false;
    }

    @IgnoreResponseBodyAdvice
    @GetMapping("/test3")
    public Boolean test3(@RequestParam(required = false) Long id) throws InterruptedException {

        return true;
    }

}
