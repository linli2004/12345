package top.tangyh.lamp.sop.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.sop.entity.SopPermGroupPermission;
import top.tangyh.lamp.sop.service.SopPermGroupPermissionService;
import top.tangyh.lamp.sop.vo.query.SopPermGroupPermissionPageQuery;
import top.tangyh.lamp.sop.vo.result.SopPermGroupPermissionResultVO;
import top.tangyh.lamp.sop.vo.save.SopPermGroupPermissionSaveVO;
import top.tangyh.lamp.sop.vo.update.SopPermGroupPermissionUpdateVO;

/**
 * <p>
 * 前端控制器
 * 组权限表
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
@RequestMapping("/sopPermGroupPermission")
@Tag(name = "组权限表")
public class SopPermGroupPermissionController extends SuperController<SopPermGroupPermissionService, Long, SopPermGroupPermission, SopPermGroupPermissionSaveVO, SopPermGroupPermissionUpdateVO, SopPermGroupPermissionPageQuery, SopPermGroupPermissionResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }


    @PostMapping("delete")
    public R<Boolean> delete(@RequestBody @Validated SopPermGroupPermissionSaveVO param) {
        return R.success(superService.delete(param.getGroupId(), param.getApiIdList()));
    }
}


