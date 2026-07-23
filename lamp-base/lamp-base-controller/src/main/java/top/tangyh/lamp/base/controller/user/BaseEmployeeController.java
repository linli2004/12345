package top.tangyh.lamp.base.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperCacheController;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.base.biz.user.BaseEmployeeBiz;
import top.tangyh.lamp.base.entity.user.BaseEmployee;
import top.tangyh.lamp.base.service.user.BaseEmployeeService;
import top.tangyh.lamp.base.vo.query.user.BaseEmployeePageQuery;
import top.tangyh.lamp.base.vo.result.user.BaseEmployeeResultVO;
import top.tangyh.lamp.base.vo.save.user.BaseEmployeeRoleRelSaveVO;
import top.tangyh.lamp.base.vo.save.user.BaseEmployeeSaveVO;
import top.tangyh.lamp.base.vo.update.user.BaseEmployeeUpdateVO;
import top.tangyh.lamp.system.vo.save.tenant.DefTenantBindUserVO;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 前端控制器
 * 员工
 * </p>
 *
 * @author zuihou
 * @date 2021-10-18
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/baseEmployee")
@Tag(name = "员工")
public class BaseEmployeeController extends SuperCacheController<BaseEmployeeService, Long, BaseEmployee, BaseEmployeeSaveVO, BaseEmployeeUpdateVO,
        BaseEmployeePageQuery, BaseEmployeeResultVO> {

    private final EchoService echoService;
    private final BaseEmployeeBiz baseEmployeeBiz;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Override
    @WebLog(value = "'分页列表查询:第' + #params?.current + '页, 显示' + #params?.size + '行'", response = false)
    public R<IPage<BaseEmployeeResultVO>> page(@RequestBody @Validated PageParams<BaseEmployeePageQuery> params) {
        IPage<BaseEmployeeResultVO> page = baseEmployeeBiz.findPageResultVO(params);
        handlerResult(page);
        return R.success(page);
    }

    @Override
    @WebLog("'查询:' + #id")
    public R<BaseEmployeeResultVO> get(@PathVariable Long id) {
        return success(baseEmployeeBiz.getEmployeeUserById(id));
    }

    /**
     * 给员工分配角色
     *
     * @param employeeRoleSaveVO 参数
     * @return 新增结果
     */
    @Operation(summary = "给员工分配角色", description = "给员工分配角色")
    @PostMapping("/employeeRole")
    @WebLog("给员工分配角色")
    public R<List<Long>> saveEmployeeRole(@RequestBody BaseEmployeeRoleRelSaveVO employeeRoleSaveVO) {
        return success(superService.saveEmployeeRole(employeeRoleSaveVO));
    }

    /**
     * 查询员工的角色
     *
     * @param employeeId 员工id
     * @return 新增结果
     */
    @Operation(summary = "查询员工的角色")
    @GetMapping("/findEmployeeRoleByEmployeeId")
    @WebLog("查询员工的角色")
    public R<List<Long>> findEmployeeRoleByEmployeeId(@RequestParam Long employeeId) {
        return success(superService.findEmployeeRoleByEmployeeId(employeeId));
    }

    @Override
    public R<BaseEmployee> handlerSave(BaseEmployeeSaveVO model) {
        return R.success(baseEmployeeBiz.save(model));
    }

    @Override
    public R<Boolean> handlerDelete(List<Long> ids) {
        return R.success(baseEmployeeBiz.delete(ids));
    }

    @Operation(summary = "运营者将非该企业的用户，添加进企业使其成为员工并设置为租户管理员")
    @PostMapping(value = "/bindUser")
    @WebLog("运营者将非该企业的用户，添加进企业使其成为员工并设置为租户管理员")
    public R<Boolean> bindUser(@RequestBody @Validated DefTenantBindUserVO param) {
        return R.success(param.getIsBind() != null && param.getIsBind() ? baseEmployeeBiz.bindUser(param) : baseEmployeeBiz.unBindUser(param));
    }

    @Operation(summary = "运营者通过租户申请，并将申请者设为管理员")
    @PostMapping(value = "/toExamineTenantAndBindUser")
    @WebLog("运营者通过租户申请，并将申请者设为管理员")
    public R<Boolean> toExamineTenantAndBindUser(@RequestBody @Validated DefTenantBindUserVO param) {
        return R.success(baseEmployeeBiz.toExamineTenantAndBindUser(param));
    }

    @Operation(summary = "租户管理员邀请用户进入企业")
    @PostMapping(value = "/invitationUser")
    @WebLog("租户管理员邀请用户进入企业")
    public R<Boolean> invitationUser(@RequestBody @Validated DefTenantBindUserVO param) {
        return R.success(param.getIsBind() != null && param.getIsBind() ? baseEmployeeBiz.invitationUser(param) : baseEmployeeBiz.unInvitationUser(param));
    }

    @Operation(summary = "运营者将用户设置为某个租户的租户管理员")
    @PostMapping(value = "/bindTenantAdmin")
    @WebLog("运营者将用户设置为某个租户的租户管理员")
    public R<Boolean> bindTenantAdmin(@RequestBody @Validated DefTenantBindUserVO param) {
        return R.success(param.getIsBind() != null && param.getIsBind() ? baseEmployeeBiz.bindTenantAdmin(param) : baseEmployeeBiz.unBindTenantAdmin(param));
    }

    @Operation(summary = "查询登录用户的id,名称，联系电话，组织信息")
    @PostMapping(value = "/getCurrentEmployee")
    @WebLog("查询登录用户的id,名称，联系电话，组织信息")
    public R<Map<String, Object>> getCurrentEmployee() {
        return R.success(baseEmployeeBiz.getCurrentUserMap());
    }

    @Operation(summary = "查询用户信息根据roleCode")
    @PostMapping(value = "/getEmployeeByRoleCode")
    @WebLog("查询用户信息根据roleCode")
    public R<List<BaseEmployeeResultVO>> getEmployeeByRoleCode(@RequestBody List<String> roleCodeList) {
        return R.success(baseEmployeeBiz.getEmployeeByRoleCode(roleCodeList));
    }

    @Operation(summary = "查询分管领导用户信息")
    @PostMapping(value = "/getLeaderEmployeeByOrgId")
    @WebLog("查询分管领导用户信息")
    public R<List<BaseEmployeeResultVO>> getLeaderEmployeeByOrgId(@RequestBody List<Long> orgIdList) {
        return R.success(baseEmployeeBiz.getLeaderEmployeeByOrgId(orgIdList));
    }
}
