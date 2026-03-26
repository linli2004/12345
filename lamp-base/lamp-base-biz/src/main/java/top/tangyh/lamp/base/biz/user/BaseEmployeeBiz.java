package top.tangyh.lamp.base.biz.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.exception.BizException;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.basic.utils.CollHelper;
import top.tangyh.lamp.base.entity.system.BaseRole;
import top.tangyh.lamp.base.entity.user.BaseEmployee;
import top.tangyh.lamp.base.entity.user.BaseEmployeeRoleRel;
import top.tangyh.lamp.base.entity.user.BaseOrg;
import top.tangyh.lamp.base.service.system.BaseRoleService;
import top.tangyh.lamp.base.service.user.BaseEmployeeOrgRelService;
import top.tangyh.lamp.base.service.user.BaseEmployeeRoleRelService;
import top.tangyh.lamp.base.service.user.BaseEmployeeService;
import top.tangyh.lamp.base.service.user.BaseOrgService;
import top.tangyh.lamp.base.vo.query.user.BaseEmployeePageQuery;
import top.tangyh.lamp.base.vo.result.user.BaseEmployeeResultVO;
import top.tangyh.lamp.base.vo.save.user.BaseEmployeeSaveVO;
import top.tangyh.lamp.common.constant.RoleConstant;
import top.tangyh.lamp.model.entity.system.SysUser;
import top.tangyh.lamp.model.enumeration.base.ActiveStatusEnum;
import top.tangyh.lamp.model.enumeration.system.DefTenantStatusEnum;
import top.tangyh.lamp.system.entity.tenant.DefTenant;
import top.tangyh.lamp.system.entity.tenant.DefUser;
import top.tangyh.lamp.system.entity.tenant.DefUserTenantRel;
import top.tangyh.lamp.system.service.tenant.DefTenantService;
import top.tangyh.lamp.system.service.tenant.DefUserService;
import top.tangyh.lamp.system.service.tenant.DefUserTenantRelService;
import top.tangyh.lamp.system.vo.query.tenant.DefUserPageQuery;
import top.tangyh.lamp.system.vo.save.tenant.DefTenantBindUserVO;
import top.tangyh.lamp.system.vo.save.tenant.DefUserSaveVO;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 员工大业务层
 *
 * @author zuihou
 * @date 2021/10/22 10:37
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BaseEmployeeBiz {
    private final BaseEmployeeService baseEmployeeService;
    private final BaseEmployeeOrgRelService baseEmployeeOrgRelService;
    private final BaseEmployeeRoleRelService baseEmployeeRoleRelService;
    private final DefUserService defUserService;
    private final DefUserTenantRelService defUserTenantRelService;
    private final DefTenantService defTenantService;
    private final BaseOrgService baseOrgService;
    private final BaseRoleService baseRoleService;

    /**
     * 根据员工ID 查询员工、用户和他所在的机构 信息
     *
     * @param employeeId 员工ID
     * @return top.tangyh.lamp.base.vo.result.user.BaseEmployeeResultVO
     * @author tangyh
     * @date 2022/10/28 12:13 AM
     * @create [2022/10/28 12:13 AM ] [tangyh] [初始创建]
     */
    public BaseEmployeeResultVO getEmployeeUserById(Long employeeId) {
        // 租户库
        BaseEmployee employee = baseEmployeeService.getById(employeeId);
        if (employee == null) {
            return null;
        }
        // def
        DefUserTenantRel utr = defUserTenantRelService.getById(employeeId);
        if (utr == null) {
            return null;
        }
        // 员工信息
        BaseEmployeeResultVO resultVO = new BaseEmployeeResultVO();
        BeanUtil.copyProperties(employee, resultVO);

        // 机构信息
        resultVO.setOrgIdList(baseEmployeeOrgRelService.findOrgIdListByEmployeeId(employeeId));

        // 用户信息
        DefUser defUser = defUserService.getById(employee.getUserId());
        resultVO.setDefUser(BeanUtil.toBean(defUser, SysUser.class));

        return resultVO;
    }

    public Map<String, String> getCurrentUserMap() {
        Long operatorId = ContextUtil.getEmployeeId();
        ArgumentAssert.notNull(operatorId, "请先登录");
        Map<String, String> responseMap = Maps.newLinkedHashMap();
        responseMap.put("operatorId", operatorId.toString());
        String operatorName = baseEmployeeService.getById(operatorId).getRealName();
        responseMap.put("operatorName", operatorName);
        DefUser defUser = defUserService.getById(ContextUtil.getUserId());
        if (null != defUser) responseMap.put("operatorPhone", defUser.getMobile());
        List<BaseOrg> orgList = baseOrgService.findOrgByEmployeeId(operatorId);
        if (CollectionUtils.isNotEmpty(orgList)) {
            BaseOrg baseOrg = orgList.get(0);
            responseMap.put("deptId", baseOrg.getId().toString());
            responseMap.put("deptName", baseOrg.getName());
        }
        List<BaseEmployeeRoleRel> employeeRoleRelList = baseEmployeeRoleRelService.list(Wraps.<BaseEmployeeRoleRel>lbQ().eq(BaseEmployeeRoleRel::getEmployeeId, operatorId));
        if (CollectionUtils.isNotEmpty(employeeRoleRelList)) {
            BaseRole baseRole = baseRoleService.getById(employeeRoleRelList.get(0).getRoleId());
            responseMap.put("roleCode", baseRole.getCode());
        }
        return responseMap;
    }

    /**
     * 删除员工
     *
     * @param ids 员工ID
     * @return java.lang.Boolean
     * @author tangyh
     * @date 2022/10/28 12:14 AM
     * @create [2022/10/28 12:14 AM ] [tangyh] [初始创建]
     */
    @GlobalTransactional
    public Boolean delete(List<Long> ids) {
        ContextUtil.setStop();
        // 删除默认库的 员工
        defUserTenantRelService.removeByIds(ids);
        // 删除基础库的 员工
        return baseEmployeeService.removeByIds(ids);
    }

    /**
     * 保存员工信息
     *
     * @param saveVO saveVO
     * @return top.tangyh.lamp.base.entity.user.BaseEmployee
     * @author tangyh
     * @date 2022/10/28 12:15 AM
     * @create [2022/10/28 12:15 AM ] [tangyh] [初始创建]
     */
    @GlobalTransactional
    public BaseEmployee save(BaseEmployeeSaveVO saveVO) {
        boolean existDefUser = defUserService.checkMobile(saveVO.getMobile(), null);
        if (existDefUser) {
            throw new BizException("手机号已被注册,请重新输入手机号 或 直接邀请它加入贵公司。");
        }
        String username = StrUtil.isBlank(saveVO.getUsername()) ? IdUtil.simpleUUID() : saveVO.getUsername();
        // 保存默认库的 用户表 和 员工表
        DefUserSaveVO userSaveVO = BeanUtil.toBean(saveVO, DefUserSaveVO.class);
        userSaveVO.setUsername(username);
        userSaveVO.setNickName(saveVO.getRealName());
        DefUserTenantRel defUserTenantRel = defUserService.saveUserAndEmployee(ContextUtil.getTenantId(), userSaveVO);

        // 保存 基础库的员工表
        saveVO.setUserId(defUserTenantRel.getUserId());
        saveVO.setId(defUserTenantRel.getId());
        saveVO.setActiveStatus(ActiveStatusEnum.ACTIVATED.getCode());
        saveVO.setIsDefault(true);
        return baseEmployeeService.save(saveVO);
    }

    /**
     * 分页查员工数据
     *
     * @param params 参数
     * @return IPage
     * @author tangyh
     * @date 2022/10/28 12:19 AM
     * @create [2022/10/28 12:19 AM ] [tangyh] [初始创建]
     */
    public IPage<BaseEmployeeResultVO> findPageResultVO(PageParams<BaseEmployeePageQuery> params) {
        BaseEmployeePageQuery pageQuery = params.getModel();
        List<Long> userIdList;
        if (!StrUtil.isAllEmpty(pageQuery.getMobile(), pageQuery.getEmail(), pageQuery.getUsername(), pageQuery.getIdCard())) {
            userIdList = defUserService.findUserIdList(BeanUtil.toBean(pageQuery, DefUserPageQuery.class));
            if (CollUtil.isEmpty(userIdList)) {
                return new Page<>(params.getCurrent(), params.getSize());
            }

            params.getModel().setUserIdList(userIdList);
        }
        IPage<BaseEmployeeResultVO> pageResultVO = baseEmployeeService.findPageResultVO(params);

        if (CollUtil.isNotEmpty(pageResultVO.getRecords())) {
            List<Long> userIds = pageResultVO.getRecords().stream().map(BaseEmployeeResultVO::getUserId).toList();
            List<DefUser> defUsers = defUserService.listByIds(userIds);
            List<SysUser> userResultVos = BeanUtil.copyToList(defUsers, SysUser.class);
            ImmutableMap<Long, SysUser> map = CollHelper.uniqueIndex(userResultVos, SysUser::getId, user -> user);

            pageResultVO.getRecords().forEach(item -> item.setDefUser(map.get(item.getUserId())));
        }

        return pageResultVO;
    }

    /**
     * 将用户绑定为租户管理员
     *
     * @param param param
     * @return java.lang.Boolean
     * @author tangyh
     * @date 2022/10/28 12:21 AM
     * @create [2022/10/28 12:21 AM ] [tangyh] [初始创建]
     */
    @GlobalTransactional
    public Boolean bindTenantAdmin(DefTenantBindUserVO param) {
        List<Long> employeeIdList = findEmployeeIdList(param);
        return baseEmployeeRoleRelService.bindRole(employeeIdList, RoleConstant.TENANT_ADMIN);
    }

    private List<Long> findEmployeeIdList(DefTenantBindUserVO param) {
        List<DefUserTenantRel> defEmployeeList = defUserTenantRelService.list(Wraps.<DefUserTenantRel>lbQ().eq(DefUserTenantRel::getTenantId, param.getTenantId()).in(DefUserTenantRel::getUserId, param.getUserIdList()));
        ArgumentAssert.notEmpty(defEmployeeList, "对不起，您选择的用户不是该企业的员工");
        List<Long> employeeIdList = defEmployeeList.stream().map(DefUserTenantRel::getId).toList();
        // 保存到指定租户的 base库的员工 + 租户管理员角色
        ContextUtil.setTenantBasePoolName(param.getTenantId());
        return employeeIdList;
    }

    /**
     * 在运营平台 将用户解绑为某个租户的 租户管理员
     *
     * @param param param
     * @return java.lang.Boolean
     * @author tangyh
     * @date 2022/10/28 12:21 AM
     * @create [2022/10/28 12:21 AM ] [tangyh] [初始创建]
     */
    @GlobalTransactional
    public Boolean unBindTenantAdmin(DefTenantBindUserVO param) {
        List<Long> employeeIdList = findEmployeeIdList(param);
        return baseEmployeeRoleRelService.unBindRole(employeeIdList, RoleConstant.TENANT_ADMIN);
    }

    /**
     * 将用户绑定某个租户的员工
     *
     * @param param param
     * @return java.lang.Boolean
     * @author tangyh
     * @date 2022/10/28 12:21 AM
     * @create [2022/10/28 12:21 AM ] [tangyh] [初始创建]
     */
    @GlobalTransactional
    public Boolean bindUser(DefTenantBindUserVO param) {
        List<BaseEmployee> baseEmployeeList = findEmployeeList(param);
        return baseEmployeeService.saveBatchBaseEmployeeAndRole(baseEmployeeList);
    }

    private List<BaseEmployee> findEmployeeList(DefTenantBindUserVO param) {
        Long tenantId = param.getTenantId();
        ArgumentAssert.notNull(tenantId, "请选择租户");


        List<DefUser> defUsers = defUserService.listByIds(param.getUserIdList());
        ArgumentAssert.notEmpty(defUsers, "请选择用户");
        long employeeCount = defUserTenantRelService.count(Wraps.<DefUserTenantRel>lbQ().eq(DefUserTenantRel::getTenantId, tenantId).in(DefUserTenantRel::getUserId, param.getUserIdList()));
        ArgumentAssert.isFalse(employeeCount > 0, "对不起，您选择的用户已经是该企业的员工");

        // 保存def库的员工
        List<DefUserTenantRel> employeeList = param.getUserIdList().stream().map(userId -> {
            DefUserTenantRel employee = new DefUserTenantRel();
            employee.setUserId(userId);
            employee.setTenantId(tenantId);
            employee.setState(true);
            employee.setIsDefault(false);
            return employee;
        }).toList();
        defUserTenantRelService.saveBatch(employeeList);

        ImmutableMap<Long, String> userMap = CollHelper.uniqueIndex(defUsers, DefUser::getId, DefUser::getNickName);
        List<BaseEmployee> baseEmployeeList = BeanUtil.copyToList(employeeList, BaseEmployee.class);
        baseEmployeeList.forEach(employee -> {
            employee.setActiveStatus(ActiveStatusEnum.ACTIVATED.getCode());
            employee.setState(true);
            employee.setRealName(userMap.get(employee.getUserId()));
        });

        // 保存到指定租户的 base库的员工 + 租户管理员角色
        ContextUtil.setTenantBasePoolName(tenantId);
        return baseEmployeeList;
    }

    /**
     * 邀请某个用户加入 他自己所在的租户
     *
     * @param param param
     * @return java.lang.Boolean
     * @author tangyh
     * @date 2022/10/28 12:22 AM
     * @create [2022/10/28 12:22 AM ] [tangyh] [初始创建]
     */
    @GlobalTransactional
    public Boolean invitationUser(DefTenantBindUserVO param) {
        Long tenantId = ContextUtil.getTenantId();
        param.setTenantId(tenantId);
        List<BaseEmployee> baseEmployeeList = findEmployeeList(param);
        return baseEmployeeService.saveBatch(baseEmployeeList);
    }

    /**
     * 在基础平台 将用户取消保定到自己所在的企业
     *
     * @param param param
     * @return java.lang.Boolean
     * @author tangyh
     * @date 2022/10/28 12:22 AM
     * @create [2022/10/28 12:22 AM ] [tangyh] [初始创建]
     * @update [2022/10/28 12:22 AM ] [tangyh] [变更描述]
     */
    @GlobalTransactional
    public Boolean unInvitationUser(DefTenantBindUserVO param) {
        Long tenantId = ContextUtil.getTenantId();
        List<Long> employeeIdList = findEmployeeIdList(param, tenantId);

        return baseEmployeeService.removeByIds(employeeIdList);
    }

    private List<Long> findEmployeeIdList(DefTenantBindUserVO param, Long tenantId) {
        List<DefUser> defUsers = defUserService.listByIds(param.getUserIdList());
        ArgumentAssert.notEmpty(defUsers, "请选择用户");
        List<DefUserTenantRel> defEmployeeList = defUserTenantRelService.list(Wraps.<DefUserTenantRel>lbQ().eq(DefUserTenantRel::getTenantId, tenantId).in(DefUserTenantRel::getUserId, param.getUserIdList()));
        ArgumentAssert.notEmpty(defEmployeeList, "对不起，您选择的用户不是该企业的员工");
        List<Long> employeeIdList = defEmployeeList.stream().map(DefUserTenantRel::getId).toList();
        defUserTenantRelService.removeByIds(employeeIdList);
        return employeeIdList;
    }

    /**
     * 在运营平台 将用户取消绑定到某个企业
     *
     * @param param param
     * @return java.lang.Boolean
     * @author tangyh
     * @date 2022/10/28 12:23 AM
     * @create [2022/10/28 12:23 AM ] [tangyh] [初始创建]
     */
    @GlobalTransactional
    public Boolean unBindUser(DefTenantBindUserVO param) {
        Long tenantId = param.getTenantId();
        ArgumentAssert.notNull(tenantId, "请选择租户");

        // 演示环境专用标识，用于WriteInterceptor拦截器判断演示环境需要禁止用户执行sql，若您无需搭建演示环境，可以删除下面一行代码
        ContextUtil.setStop();

        List<Long> employeeIdList = findEmployeeIdList(param, tenantId);

        ContextUtil.setTenantBasePoolName(tenantId);
        return baseEmployeeService.removeByIds(employeeIdList);
    }

    @GlobalTransactional
    public Boolean toExamineTenantAndBindUser(DefTenantBindUserVO param) {
        DefTenant defTenant = defTenantService.getById(param.getTenantId());
        ArgumentAssert.notNull(defTenant, "你要审核的租户不存在");

        defTenantService.updateStatus(param.getTenantId(), DefTenantStatusEnum.AGREED.getCode(), param.getReviewComments());
        param.setUserIdList(Collections.singletonList(defTenant.getCreatedBy()));
        return bindUser(param);
    }

    public List<BaseEmployeeResultVO> getEmployeeByRoleCode(List<String> roleCodeList) {
        return baseEmployeeService.getEmployeeIdByRoleCodeAndOrgId(roleCodeList, null);
    }
}
