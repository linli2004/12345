package top.tangyh.lamp.oauth.biz;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.exception.UnauthorizedException;
import top.tangyh.lamp.base.entity.user.BaseEmployee;
import top.tangyh.lamp.base.service.user.BaseEmployeeService;
import top.tangyh.lamp.base.vo.result.user.BaseEmployeeResultVO;
import top.tangyh.lamp.common.constant.AppendixType;
import top.tangyh.lamp.file.service.AppendixService;
import top.tangyh.lamp.model.vo.result.AppendixResultVO;
import top.tangyh.lamp.oauth.vo.result.DefUserInfoResultVO;
import top.tangyh.lamp.system.entity.application.DefApplication;
import top.tangyh.lamp.system.entity.tenant.DefUser;
import top.tangyh.lamp.system.entity.tenant.DefUserTenantRel;
import top.tangyh.lamp.system.service.application.DefApplicationService;
import top.tangyh.lamp.system.service.tenant.DefUserService;
import top.tangyh.lamp.system.service.tenant.DefUserTenantRelService;
import top.tangyh.lamp.system.vo.result.application.DefApplicationResultVO;

/**
 * 用户大业务
 *
 * @author zuihou
 * @date 2021/10/28 13:09
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OauthUserBiz {
    private final BaseEmployeeService baseEmployeeService;
    private final DefUserTenantRelService defUserTenantRelService;
    private final DefUserService defUserService;
    private final DefApplicationService defApplicationService;
    private final AppendixService appendixService;

    public DefUserInfoResultVO getUserById(Long id) {
        // 查默认库
        DefUser defUser = defUserService.getByIdCache(id);
        if (defUser == null) {
            log.warn("user not found, id:{}", id);
            return null;
        }

        // 用户信息
        DefUserInfoResultVO resultVO = new DefUserInfoResultVO();
        BeanUtil.copyProperties(defUser, resultVO);

        // 用户头像
        AppendixResultVO appendix = appendixService.getByBiz(defUser.getId(), AppendixType.System.DEF__USER__AVATAR);
        if (appendix != null) {
            resultVO.setAvatarId(appendix.getId());
        }

        resultVO.setTenantId(ContextUtil.getTenantId());

        if (!ContextUtil.isEmptyBasePoolNameHeader() && !ContextUtil.isEmptyEmployeeId()) {
            Long employeeId = ContextUtil.getEmployeeId();
            resultVO.setEmployeeId(employeeId);

            // 默认库的员工不存在说明该用户不属于该企业，就不能查 租户库的数据
            DefUserTenantRel defEmployee = defUserTenantRelService.getByIdCache(employeeId);
            if (defEmployee != null) {
                try {
                    //查 租户库
                    BaseEmployee employee = baseEmployeeService.getByIdCache(employeeId);
                    resultVO.setBaseEmployee(BeanUtil.toBean(employee, BaseEmployeeResultVO.class));
                } catch (Exception e) {
                    log.warn("查询您的身份信息失败，请重新登录", e);
                    // 报错说明 租户库没创建
                    throw UnauthorizedException.wrap("查询您的身份信息失败，请重新登录");
                }
            }
        }

        DefApplication defApplication = defApplicationService.getDefApp(id);
        resultVO.setDefApplication(BeanUtil.toBean(defApplication, DefApplicationResultVO.class));
        return resultVO;
    }
}
