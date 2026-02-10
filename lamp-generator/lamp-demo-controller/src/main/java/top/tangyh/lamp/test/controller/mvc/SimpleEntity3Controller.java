package top.tangyh.lamp.test.controller.mvc;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.controller.SuperReadController;
import top.tangyh.lamp.test.entity.DefGenTestSimple;
import top.tangyh.lamp.test.service2.SimpleService;
import top.tangyh.lamp.test.vo.query.DefGenTestSimplePageQuery;
import top.tangyh.lamp.test.vo.result.DefGenTestSimpleResultVO;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mvc3")
@Tag(name = "mvc3")
@WebLog(enabled = false)
public class SimpleEntity3Controller extends SuperReadController<SimpleService, Long, DefGenTestSimple, DefGenTestSimplePageQuery, DefGenTestSimpleResultVO> {

}
