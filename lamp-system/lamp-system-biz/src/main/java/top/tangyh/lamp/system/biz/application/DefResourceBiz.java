package top.tangyh.lamp.system.biz.application;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.lamp.system.entity.tenant.DefTenant;
import top.tangyh.lamp.system.service.application.DefResourceService;
import top.tangyh.lamp.system.service.tenant.DefTenantService;

import java.util.List;

/**
 * @author zuihou
 * @date 2021/11/17 15:23
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DefResourceBiz {
    private final DefResourceService defResourceService;
    private final DefTenantService defTenantService;


    @GlobalTransactional
    public boolean removeByIdWithCache(List<Long> ids) {
        boolean result = defResourceService.removeByIdWithCache(ids);

        // TODO 删除租户下的 角色资源关系表 员工资源关系表
        List<DefTenant> list = defTenantService.findNormalTenant();
        list.forEach(item -> {
            ContextUtil.setTenantBasePoolName(item.getId());
            defResourceService.deleteRoleResourceRelByResourceId(ids);
        });
        return result;
    }


}
