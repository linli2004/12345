package top.tangyh.lamp.system.manager.tenant.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.system.entity.tenant.DefTenantDatasourceConfigRel;
import top.tangyh.lamp.system.manager.tenant.DefTenantDatasourceConfigRelManager;
import top.tangyh.lamp.system.mapper.tenant.DefTenantDatasourceConfigRelMapper;

/**
 * 应用管理
 *
 * @author tangyh
 * @version v1.0
 * @date 2021/9/29 1:26 下午
 * @create [2021/9/29 1:26 下午 ] [tangyh] [初始创建]
 */
@RequiredArgsConstructor
@Service
public class DefTenantDatasourceConfigRelManagerImpl extends SuperManagerImpl<DefTenantDatasourceConfigRelMapper, DefTenantDatasourceConfigRel> implements DefTenantDatasourceConfigRelManager {

}
