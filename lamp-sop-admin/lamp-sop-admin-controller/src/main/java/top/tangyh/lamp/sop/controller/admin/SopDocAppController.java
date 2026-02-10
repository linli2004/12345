package top.tangyh.lamp.sop.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.sop.entity.SopDocApp;
import top.tangyh.lamp.sop.service.SopDocAppService;
import top.tangyh.lamp.sop.vo.query.SopDocAppPageQuery;
import top.tangyh.lamp.sop.vo.result.SopDocAppResultVO;
import top.tangyh.lamp.sop.vo.save.SopDocAppSaveVO;
import top.tangyh.lamp.sop.vo.update.SopDocAppUpdateVO;

/**
 * <p>
 * 前端控制器
 * 文档应用
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/sopDocApp")
@Tag(name = "文档应用")
public class SopDocAppController extends SuperController<SopDocAppService, Long, SopDocApp, SopDocAppSaveVO, SopDocAppUpdateVO, SopDocAppPageQuery, SopDocAppResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }


}


