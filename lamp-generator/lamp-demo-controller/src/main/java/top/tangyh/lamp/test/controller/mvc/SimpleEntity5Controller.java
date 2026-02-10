package top.tangyh.lamp.test.controller.mvc;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.controller.SuperSimpleController;
import top.tangyh.lamp.test.entity.DefGenTestSimple;
import top.tangyh.lamp.test.service2.SimpleService;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mvc5")
@Tag(name = "mvc5")
@WebLog(enabled = false)
public class SimpleEntity5Controller extends SuperSimpleController<SimpleService, Long, DefGenTestSimple> {

}
