package top.tangyh.lamp.base.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.base.entity.RoleStatusMapping;
import top.tangyh.lamp.base.service.RoleStatusMappingService;
import top.tangyh.lamp.base.vo.query.RoleStatusMappingPageQuery;
import top.tangyh.lamp.base.vo.result.RoleStatusMappingResultVO;
import top.tangyh.lamp.base.vo.save.RoleStatusMappingSaveVO;
import top.tangyh.lamp.base.vo.update.RoleStatusMappingUpdateVO;

/**
 * <p>
 * 前端控制器
 * 角色视图映射配置表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:49:32
 * @create [2026-03-03 11:49:32] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/roleStatusMapping")
@Tag(name = "角色视图映射配置表")
public class RoleStatusMappingController extends SuperController<RoleStatusMappingService, Long, RoleStatusMapping
        , RoleStatusMappingSaveVO, RoleStatusMappingUpdateVO, RoleStatusMappingPageQuery, RoleStatusMappingResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

}


