package top.tangyh.lamp.base.controller.anyone;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.lamp.system.service.tenant.DefUserTenantRelService;

/**
 * @author zuihou
 * @date 2021/10/26 21:40
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/anyone")
@Tag(name = "需要登录但无需验证uri权限的接口")
public class BaseAnyoneController {

    private final DefUserTenantRelService defUserTenantRelService;

    @GetMapping("/base/test")
    public R<Object> test(@RequestParam(required = false) Long id) throws InterruptedException {
        SaSession session = StpUtil.getSession();
        log.info("id={}", session);
        Thread.sleep(id);
        log.info("id={}", id);
        return R.success(id);
    }

    @Operation(summary = "设置默认企业")
    @PutMapping("/updateDefaultTenant")
    @WebLog(value = "设置默认企业")
    public R<Boolean> updateDefaultTenant(@RequestParam Long tenantId) {
        return R.success(defUserTenantRelService.updateDefaultTenant(tenantId, ContextUtil.getUserId()));
    }
}
