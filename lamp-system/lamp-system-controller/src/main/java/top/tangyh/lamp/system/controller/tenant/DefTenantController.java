package top.tangyh.lamp.system.controller.tenant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperCacheController;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.model.enumeration.system.DefTenantStatusEnum;
import top.tangyh.lamp.system.entity.tenant.DefTenant;
import top.tangyh.lamp.system.service.tenant.DefTenantService;
import top.tangyh.lamp.system.vo.query.tenant.DefTenantPageQuery;
import top.tangyh.lamp.system.vo.result.tenant.DefTenantResultVO;
import top.tangyh.lamp.system.vo.save.tenant.DefTenantInitVO;
import top.tangyh.lamp.system.vo.save.tenant.DefTenantSaveVO;
import top.tangyh.lamp.system.vo.update.tenant.DefTenantUpdateVO;

import java.util.List;


/**
 * <p>
 * 前端控制器
 * 企业
 * </p>
 *
 * @author zuihou
 * @date 2021-09-13
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/defTenant")
@Tag(name = "企业")
public class DefTenantController extends SuperCacheController<DefTenantService, Long, DefTenant, DefTenantSaveVO,
        DefTenantUpdateVO, DefTenantPageQuery, DefTenantResultVO> {

    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Operation(summary = "查询所有企业", description = "查询所有企业")
    @GetMapping("/all")
    @WebLog("查询所有企业")
    public R<List<DefTenant>> list() {
        return success(superService.list(Wraps.<DefTenant>lbQ().eq(DefTenant::getStatus, DefTenantStatusEnum.NORMAL)));
    }

    @Operation(summary = "检测租户是否存在", description = "检测租户是否存在")
    @GetMapping("/check/{code}")
    @WebLog("检测租户是否存在")
    public R<Boolean> check(@PathVariable("code") String code) {
        return success(superService.check(code));
    }


    @Override
    public R<Boolean> handlerDelete(List<Long> ids) {
        // 这个操作相当的危险，请谨慎操作！！!
        return success(superService.delete(ids));
    }

    @Operation(summary = "删除租户和基础租户数据，请谨慎操作")
    @DeleteMapping("/deleteAll")
    @WebLog("删除租户和基础租户数据")
    public R<Boolean> deleteAll(@RequestBody List<Long> ids) {
        // 这个操作相当的危险，请谨慎操作！！!
        return success(superService.deleteAll(ids));
    }

    @Operation(summary = "修改租户审核状态", description = "修改租户审核状态")
    @PostMapping("/updateStatus")
    @WebLog("修改租户审核状态")
    public R<Boolean> updateStatus(@NotNull(message = "请修改正确的企业") @RequestParam Long id,
                                   @RequestParam @NotNull(message = "请传递状态值") String status,
                                   @RequestParam(required = false) String reviewComments) {
        return success(superService.updateStatus(id, status, reviewComments));
    }

    @Operation(summary = "修改租户状态", description = "修改租户状态")
    @PostMapping("/updateState")
    @WebLog("修改租户状态")
    public R<Boolean> updateState(@NotNull(message = "请修改正确的企业") @RequestParam Long id,
                                  @RequestParam @NotNull(message = "请传递状态值") Boolean state) {
        return success(superService.updateState(id, state));
    }

    /**
     * 初始化数据
     */
    @Operation(summary = "初始化数据", description = "初始化数据")
    @PostMapping("/initData")
    @WebLog("连接数据源")
    public R<Boolean> initData(@Validated @RequestBody DefTenantInitVO tenantConnect) {
        return success(superService.initData(tenantConnect));
    }

}
