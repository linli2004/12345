package top.tangyh.lamp.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.tangyh.basic.base.R;

/**
 * 文件接口
 *
 * @author zuihou
 * @date 2019/06/21
 */
//@FeignClient(name = "lamp-generator-server")
public interface GrayApi {
    /**
     * 测试方法
     *
     * @param id id
     * @return
     */

    @GetMapping("/gray/test2")
    R<Object> test2(@RequestParam(required = false) Long id);

}
