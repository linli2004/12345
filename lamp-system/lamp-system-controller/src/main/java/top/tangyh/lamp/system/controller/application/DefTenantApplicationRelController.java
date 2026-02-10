package top.tangyh.lamp.system.controller.application;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.annotation.user.LoginUser;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperReadController;
import top.tangyh.basic.base.entity.SuperEntity;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.model.entity.system.SysUser;
import top.tangyh.lamp.system.entity.application.DefTenantApplicationRel;
import top.tangyh.lamp.system.service.application.DefTenantApplicationRelService;
import top.tangyh.lamp.system.vo.query.application.DefTenantApplicationRelPageQuery;
import top.tangyh.lamp.system.vo.result.application.DefTenantApplicationRelResultVO;
import top.tangyh.lamp.system.vo.save.application.DefTenantApplicationRelSaveVO;
import top.tangyh.lamp.system.vo.update.application.DefTenantApplicationRelUpdateVO;

import java.time.LocalDateTime;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * 租户应用授权
 * </p>
 *
 * @author zuihou
 * @date 2021-09-15
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/defTenantApplicationRel")
@Tag(name = "租户应用授权")
public class DefTenantApplicationRelController extends SuperReadController
        <DefTenantApplicationRelService, Long, DefTenantApplicationRel, DefTenantApplicationRelPageQuery, DefTenantApplicationRelResultVO> {

    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Override
    public void handlerResult(IPage<DefTenantApplicationRelResultVO> page) {
        super.handlerResult(page);
        // true 表示已过期
        page.getRecords().forEach(item ->
                item.setExpired(item.getExpirationTime() != null && item.getExpirationTime().isBefore(LocalDateTime.now()))
        );
    }

    @Override
    public R<DefTenantApplicationRelResultVO> getDetail(@RequestParam("id") Long id) {
        return R.success(superService.getDetailById(id));
    }

    @Operation(summary = "授权")
    @PostMapping(value = "/grant")
    @WebLog(value = "授权")
    public R<Boolean> grant(@RequestBody @Validated DefTenantApplicationRelSaveVO saveVO, @Parameter(hidden = true) @LoginUser(isUser = true) SysUser sysUser) {
        return success(superService.grant(saveVO, sysUser));
    }

    @Operation(summary = "取消授权")
    @PostMapping(value = "/cancel")
    @WebLog(value = "取消授权")
    public R<Boolean> cancel(@RequestBody List<Long> ids, @Parameter(hidden = true) @LoginUser(isUser = true) SysUser sysUser) {
        return success(superService.cancel(ids, sysUser));
    }


    @Operation(summary = "续期")
    @PostMapping(value = "/renewal")
    @WebLog(value = "续期")
    public R<Boolean> renewal(@RequestBody @Validated(SuperEntity.Update.class) DefTenantApplicationRelUpdateVO updateVO, @Parameter(hidden = true) @LoginUser(isUser = true) SysUser sysUser) {
        return success(superService.renewal(updateVO, sysUser));
    }


}
