package top.tangyh.lamp.sop.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.sop.entity.SopSysConfig;
import top.tangyh.lamp.sop.service.SopSysConfigService;
import top.tangyh.lamp.sop.vo.query.SopSysConfigPageQuery;
import top.tangyh.lamp.sop.vo.result.SopSysConfigResultVO;
import top.tangyh.lamp.sop.vo.save.SopSysConfigSaveVO;
import top.tangyh.lamp.sop.vo.update.SopSysConfigUpdateVO;

/**
 * <p>
 * 前端控制器
 * 系统配置表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:44
 *
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/sopSysConfig")
@Tag(name = "系统配置表")
public class SopSysConfigController extends SuperController<SopSysConfigService, Long, SopSysConfig, SopSysConfigSaveVO, SopSysConfigUpdateVO, SopSysConfigPageQuery, SopSysConfigResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

}


