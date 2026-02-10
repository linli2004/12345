package top.tangyh.lamp.test.controller.cache;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperCacheController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.test.entity.DefGenTestSimple;
import top.tangyh.lamp.test.manager.DefGenTestSimpleCacheManager;
import top.tangyh.lamp.test.service.DefGenTestSimpleCacheService;
import top.tangyh.lamp.test.vo.query.DefGenTestSimplePageQuery;
import top.tangyh.lamp.test.vo.result.DefGenTestSimpleResultVO;
import top.tangyh.lamp.test.vo.save.DefGenTestSimpleSaveVO;
import top.tangyh.lamp.test.vo.update.DefGenTestSimpleUpdateVO;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/CacheController")
@Tag(name = "缓存演示")
public class CacheController
        extends SuperCacheController
        <DefGenTestSimpleCacheService, Long, DefGenTestSimple, DefGenTestSimpleSaveVO,
                DefGenTestSimpleUpdateVO, DefGenTestSimplePageQuery, DefGenTestSimpleResultVO> {
    private final EchoService echoService;
    private final DefGenTestSimpleCacheManager defGenTestSimpleCacheManager;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Operation(summary = "AAA插入")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public R<Object> insert(@RequestBody DefGenTestSimple info) {
        log.info("info={}", info);
        defGenTestSimpleCacheManager.insert(info);
        return R.success(info);
    }

}
