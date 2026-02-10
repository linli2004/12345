package top.tangyh.lamp.test.controller.mvc;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.controller.SuperCacheController;
import top.tangyh.lamp.test.entity.DefGenTestSimple;
import top.tangyh.lamp.test.service2.SimpleCacheService;
import top.tangyh.lamp.test.vo.query.DefGenTestSimplePageQuery;
import top.tangyh.lamp.test.vo.result.DefGenTestSimpleResultVO;
import top.tangyh.lamp.test.vo.save.DefGenTestSimpleSaveVO;
import top.tangyh.lamp.test.vo.update.DefGenTestSimpleUpdateVO;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mvc2")
@Tag(name = "mvc2")
@WebLog(enabled = false)
public class SimpleEntity2Controller extends SuperCacheController<SimpleCacheService, Long, DefGenTestSimple, DefGenTestSimpleSaveVO, DefGenTestSimpleUpdateVO, DefGenTestSimplePageQuery, DefGenTestSimpleResultVO> {

}
