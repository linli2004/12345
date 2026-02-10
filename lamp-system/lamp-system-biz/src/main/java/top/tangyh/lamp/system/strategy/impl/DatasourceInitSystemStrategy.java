package top.tangyh.lamp.system.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.model.enumeration.system.TenantConnectTypeEnum;
import top.tangyh.lamp.system.entity.tenant.DefDatasourceConfig;
import top.tangyh.lamp.system.entity.tenant.DefTenantDatasourceConfigRel;
import top.tangyh.lamp.system.manager.tenant.DefDatasourceConfigManager;
import top.tangyh.lamp.system.manager.tenant.DefTenantDatasourceConfigRelManager;
import top.tangyh.lamp.system.strategy.InitSystemStrategy;
import top.tangyh.lamp.system.vo.save.tenant.DefTenantInitVO;
import top.tangyh.lamp.tenant.service.DataSourceService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static top.tangyh.basic.context.ContextConstants.TENANT_BASE_POOL_NAME_HEADER;
import static top.tangyh.basic.context.ContextConstants.TENANT_EXTEND_POOL_NAME_HEADER;

/**
 * 初始化系统
 * <p>
 * 初始化规则：
 * lamp-authority-server/src/main/resources/sql 路径存放8个sql文件 (每个库对应一个文件)
 * lamp_base.sql            # 基础库：权限、消息，短信，邮件，文件等
 * data_lamp_base.sql       # 基础库数据： 如初始用户，初始角色，初始菜单
 *
 * @author zuihou
 * @date 2019/10/25
 */
@Service("DATASOURCE")
@Slf4j
@RequiredArgsConstructor
public class DatasourceInitSystemStrategy implements InitSystemStrategy {
    private final DefDatasourceConfigManager defDatasourceConfigManager;
    private final DefTenantDatasourceConfigRelManager defTenantDatasourceConfigRelManager;
    private final DataSourceService dataSourceService;

    /**
     * 求优化！
     *
     * @param tenantInitVO 链接信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initData(DefTenantInitVO tenantInitVO) {
        if (TenantConnectTypeEnum.CUSTOM.eq(tenantInitVO.getConnectType())) {
            ArgumentAssert.notNull(tenantInitVO.getBaseDatasourceId(), "请配置基础库数据库链接信息");
            ArgumentAssert.notNull(tenantInitVO.getExtendDatasourceId(), "请配置扩展库数据库链接信息");
            List<Long> ids = Arrays.asList(tenantInitVO.getBaseDatasourceId(), tenantInitVO.getExtendDatasourceId());
            List<DefDatasourceConfig> dcList = defDatasourceConfigManager.listByIds(ids);
            ArgumentAssert.notEmpty(dcList, "请配置正确的基础库和扩展库数据库链接信息");

            defTenantDatasourceConfigRelManager.remove(Wraps.<DefTenantDatasourceConfigRel>lbQ().eq(DefTenantDatasourceConfigRel::getTenantId, tenantInitVO.getId()));
            List<DefTenantDatasourceConfigRel> list = new ArrayList<>();
            list.add(DefTenantDatasourceConfigRel.builder().dbPrefix(TENANT_BASE_POOL_NAME_HEADER).tenantId(tenantInitVO.getId()).datasourceConfigId(tenantInitVO.getBaseDatasourceId()).build());
            list.add(DefTenantDatasourceConfigRel.builder().dbPrefix(TENANT_EXTEND_POOL_NAME_HEADER).tenantId(tenantInitVO.getId()).datasourceConfigId(tenantInitVO.getExtendDatasourceId()).build());
            defTenantDatasourceConfigRelManager.saveBatch(list);

            dataSourceService.addCustomDsAndData(tenantInitVO.getId());
        } else if (TenantConnectTypeEnum.SYSTEM.eq(tenantInitVO.getConnectType())) {
            // 本地模式： 在自己的数据库中 实时新建 SCHEMA， 远程模式：需要自己先创建好数据库，并指定地址
            dataSourceService.createDatabase(tenantInitVO.getId());
            // 2. 动态添加所有数据源链接 & 创建表 & 初始数据
            dataSourceService.addSystemDsAndData(tenantInitVO.getId());
        }
        return true;
    }


    @Override
    public boolean reset(String tenant) {

        return true;
    }

    @Override
    public boolean delete(List<Long> ids) {
        ids.forEach(dataSourceService::removeDbAndDs);
        return true;
    }
}
