package top.tangyh.lamp.base.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.base.entity.RemindTemplate;
import top.tangyh.lamp.base.service.RemindTemplateService;
import top.tangyh.lamp.base.vo.query.RemindTemplatePageQuery;
import top.tangyh.lamp.base.vo.result.RemindTemplateResultVO;
import top.tangyh.lamp.base.vo.save.RemindTemplateSaveVO;
import top.tangyh.lamp.base.vo.update.RemindTemplateUpdateVO;

/**
 * <p>
 * 前端控制器
 * 提醒模板
 * </p>
 *
 * @author lunar
 * @date 2026-03-10 08:39:01
 * @create [2026-03-10 08:39:01] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/remindTemplate")
@Tag(name = "提醒模板")
public class RemindTemplateController extends SuperController<RemindTemplateService, Long, RemindTemplate
        , RemindTemplateSaveVO, RemindTemplateUpdateVO, RemindTemplatePageQuery, RemindTemplateResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

}


