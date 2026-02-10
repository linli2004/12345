package top.tangyh.lamp.msg.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.msg.biz.ExtendMsgTemplateBiz;
import top.tangyh.lamp.msg.entity.ExtendMsgTemplate;
import top.tangyh.lamp.msg.service.ExtendMsgTemplateService;
import top.tangyh.lamp.msg.vo.query.ExtendMsgTemplatePageQuery;
import top.tangyh.lamp.msg.vo.result.ExtendMsgTemplateResultVO;
import top.tangyh.lamp.msg.vo.save.ExtendMsgTemplateSaveVO;
import top.tangyh.lamp.msg.vo.update.ExtendMsgTemplateUpdateVO;

import java.util.List;

import static top.tangyh.lamp.common.constant.SwaggerConstants.DATA_TYPE_LONG;
import static top.tangyh.lamp.common.constant.SwaggerConstants.DATA_TYPE_STRING;

/**
 * <p>
 * 前端控制器
 * 消息模板
 * </p>
 *
 * @author zuihou
 * @date 2022-07-04 15:51:37
 * @create [2022-07-04 15:51:37] [zuihou] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/extendMsgTemplate")
@Tag(name = "消息模板")
public class ExtendMsgTemplateController extends SuperController<ExtendMsgTemplateService, Long, ExtendMsgTemplate, ExtendMsgTemplateSaveVO,
        ExtendMsgTemplateUpdateVO, ExtendMsgTemplatePageQuery, ExtendMsgTemplateResultVO> {
    private final EchoService echoService;
    private final ExtendMsgTemplateBiz extendMsgTemplateBiz;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Parameters({
            @Parameter(name = "id", description = "ID", schema = @Schema(type = DATA_TYPE_LONG), in = ParameterIn.QUERY),
            @Parameter(name = "code", description = "编码", schema = @Schema(type = DATA_TYPE_STRING), in = ParameterIn.QUERY),
    })
    @Operation(summary = "检测资源编码是否可用", description = "检测资源编码是否可用")
    @GetMapping("/check")
    public R<Boolean> check(@RequestParam(required = false) Long id, @RequestParam String code) {
        return success(superService.check(code, id));
    }

    @Operation(summary = "导入系统消息模板", description = "导入系统消息模板")
    @PostMapping("/importMsgTemplate")
    public R<Boolean> importMsgTemplate(@RequestBody List<Long> idList) {
        return success(extendMsgTemplateBiz.importMsgTemplate(idList));
    }
}


