package top.tangyh.lamp.sop.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.sop.entity.SopDocContent;
import top.tangyh.lamp.sop.service.SopDocContentService;
import top.tangyh.lamp.sop.vo.query.SopDocContentPageQuery;
import top.tangyh.lamp.sop.vo.result.SopDocContentResultVO;
import top.tangyh.lamp.sop.vo.save.SopDocContentSaveVO;
import top.tangyh.lamp.sop.vo.update.SopDocContentUpdateVO;

/**
 * <p>
 * 前端控制器
 * 文档内容
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
@RequestMapping("/sopDocContent")
@Tag(name = "文档内容")
public class SopDocContentController extends SuperController<SopDocContentService, Long, SopDocContent, SopDocContentSaveVO, SopDocContentUpdateVO, SopDocContentPageQuery, SopDocContentResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

}


