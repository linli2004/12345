package top.tangyh.lamp.tenant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DatasourceInitProperties;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.druid.DruidConfig;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.exception.ErrorCreateDataSourceException;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.annotation.DbType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.tangyh.basic.context.ContextConstants;
import top.tangyh.basic.database.properties.DatabaseProperties;
import top.tangyh.basic.exception.BizException;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.basic.utils.DbPlusUtil;
import top.tangyh.lamp.model.enumeration.system.DefTenantStatusEnum;
import top.tangyh.lamp.model.enumeration.system.TenantConnectTypeEnum;
import top.tangyh.lamp.tenant.dynamic.processor.DsThreadProcessor;
import top.tangyh.lamp.tenant.mapper.InitDatabaseMapper;
import top.tangyh.lamp.tenant.model.DefDatasourceConfigBO;
import top.tangyh.lamp.tenant.model.DefTenantBO;
import top.tangyh.lamp.tenant.service.DataSourceService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 数据源管理
 * <p>
 * lamp.database.multiTenantType=DATASOURCE 时，该类才会生效
 *
 * @author zuihou
 * @date 2020年03月15日11:35:08
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DynamicDataSourceServiceImpl implements DataSourceService {
    private static final String SCHEMA_PATH = "schema/{}/{}.sql";
    private static final String DATA_PATH = "data/{}/{}.sql";
    private final DataSource dataSource;
    private final DefaultDataSourceCreator defaultDataSourceCreator;
    private final DatabaseProperties databaseProperties;
    private final DynamicDataSourceProperties properties;
    private final InitDatabaseMapper initDbMapper;
    @Value("${lamp.dameng.username:}")
    private String username;

    @Override
    public Set<String> findAll() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        return ds.getDataSources().keySet();
    }

    @Override
    public boolean check(Long tenantId) {
        List<String> initDatabasePrefix = databaseProperties.getInitDatabasePrefix();
        ArgumentAssert.notEmpty(initDatabasePrefix, "请先配置lamp.database.initDatabasePrefix");
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        Map<String, DataSource> dataSourceMap = ds.getDataSources();
        return initDatabasePrefix.stream().allMatch(prefix ->
                dataSourceMap.containsKey(DsThreadProcessor.getPoolName(prefix, String.valueOf(tenantId)))
        );
    }

    @Override
    public boolean removeDbAndDs(Long tenantId) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        databaseProperties.getInitDatabasePrefix().forEach(prefix -> {
            String database = DsThreadProcessor.getPoolName(prefix, String.valueOf(tenantId));
            if (StrUtil.isNotEmpty(database)) {
                initDbMapper.dropDatabase(database);
            }
            ds.removeDataSource(database);
        });
        return true;
    }

    /**
     * 测试链接
     *
     * @param dataSourceProperty
     * @return
     */
    @Override
    public boolean testConnection(DataSourceProperty dataSourceProperty) {
        dataSourceProperty.setSeata(false);
        dataSourceProperty.setDruid(BeanUtil.toBean(properties.getDruid(), DruidConfig.class));
        // 配置获取链接等待超时的时间
        dataSourceProperty.getDruid().setMaxWait(3000);
        // 配置初始化大小、最小、最大
        dataSourceProperty.getDruid().setInitialSize(1);
        dataSourceProperty.getDruid().setMinIdle(1);
        dataSourceProperty.getDruid().setMaxActive(1);
        // 链接错误重试次数
        dataSourceProperty.getDruid().setConnectionErrorRetryAttempts(0);
        // 获取失败后中断
        dataSourceProperty.getDruid().setBreakAfterAcquireFailure(true);

        DataSource testDataSource = null;
        Connection connection = null;
        boolean flag;
        try {
            testDataSource = defaultDataSourceCreator.createDataSource(dataSourceProperty);
            connection = testDataSource.getConnection();

            int timeOut = 5;
            if (connection == null || connection.isClosed() || !connection.isValid(timeOut)) {
                log.info("链接已关闭或无效，请重试获取链接！");
                connection = testDataSource.getConnection();
            }
            flag = connection != null;
        } catch (ErrorCreateDataSourceException e) {
            log.error("数据源初始化期间出现异常", e);
            throw new BizException("数据源初始化期间出现异常", e);
        } catch (Exception e) {
            log.error("创建测试链接错误 {}", dataSourceProperty.getUrl());
            throw new BizException("创建测试链接错误 " + dataSourceProperty.getUrl(), e);
        } finally {
            if (testDataSource instanceof ItemDataSource ids) {
                ids.close();
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.warn("关闭测试数据源链接异常", e);
                }
            }
        }
        return flag;
    }

    /**
     * 初始化数据库
     *
     * @param tenantId 明文租户编码
     */
    @Override
    public void createDatabase(Long tenantId) {
        if (CollUtil.isEmpty(databaseProperties.getInitDatabasePrefix())) {
            throw BizException.wrap("请先配置需要动态创建的数据库 库名前缀");
        }

        databaseProperties.getInitDatabasePrefix().forEach(database -> {
            String databaseName = DsThreadProcessor.getPoolName(database, String.valueOf(tenantId));
            this.initDbMapper.createDatabase(databaseName, username);
            initDbMapper.grant(databaseName);
        });

    }


    @Override
    public boolean loadSystemDataSource() {
        List<String> status = Arrays.asList(DefTenantStatusEnum.NORMAL.getCode(), DefTenantStatusEnum.WAIT_INIT_DATASOURCE.getCode());
        List<Long> list = initDbMapper.selectTenantCodeList(status, TenantConnectTypeEnum.SYSTEM.name());
        list.forEach(tenantId -> addSystem(tenantId, false, false));
        return true;
    }

    @Override
    public boolean loadCustomDataSource() {
        List<String> status = Arrays.asList(DefTenantStatusEnum.NORMAL.getCode(), DefTenantStatusEnum.WAIT_INIT_DATASOURCE.getCode());
        List<DefDatasourceConfigBO> dcList = initDbMapper.selectDataSourceConfig(status, TenantConnectTypeEnum.CUSTOM.name());
        return addCustom(dcList, false, false);
    }

    @Override
    public boolean initDataSource(Long tenantId) {
        DefTenantBO tenant = initDbMapper.getTenantById(tenantId);
        ArgumentAssert.notNull(tenant, "您要初始化的租户不存在");
        if (TenantConnectTypeEnum.CUSTOM.eq(tenant.getConnectType())) {
            List<DefDatasourceConfigBO> dcList = initDbMapper.selectDataSourceConfigByTenantId(tenant.getId());
            return addCustom(dcList, false, true);
        } else {
            return addSystem(tenantId, false, true);
        }
    }

    @Override
    public boolean addSystemDsAndData(Long tenantId) {
        return addSystem(tenantId, true, true);
    }

    @Override
    public boolean addCustomDsAndData(Long tenantId) {
        List<DefDatasourceConfigBO> dcList = initDbMapper.selectDataSourceConfigByTenantId(tenantId);
        return addCustom(dcList, true, true);
    }

    private DbType getDbType() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource primary = ds.getDataSource(null);
        return DbPlusUtil.getDbType(primary);
    }

    private boolean addCustom(List<DefDatasourceConfigBO> dcList, boolean isInitSchema, boolean isNotErrorRetry) {
        if (CollUtil.isEmpty(dcList)) {
            return false;
        }
        DataSourceProperty defDataSourceProperty = properties.getDatasource().get(ContextConstants.DEF_TENANT_ID_STR);
        ArgumentAssert.notNull(defDataSourceProperty, "请先配置默认[{}]数据源", ContextConstants.DEF_TENANT_ID_STR);
        // REMOTE 类型的数据源初始化
        dcList.forEach(dc -> {
            DataSourceProperty dataSourceProperty = new DataSourceProperty();
            BeanUtils.copyProperties(defDataSourceProperty, dataSourceProperty);
            BeanUtils.copyProperties(dc, dataSourceProperty);
            dataSourceProperty.setPoolName(DsThreadProcessor.getPoolName(dc.getDbPrefix(), String.valueOf(dc.getTenantId())));
            if (isInitSchema) {
                DatasourceInitProperties init = dataSourceProperty.getInit();
                if (init == null) {
                    init = new DatasourceInitProperties();
                }
                init.setSchema(StrUtil.format(SCHEMA_PATH, getDbType().getDb(), dc.getDbPrefix()));
                init.setData(StrUtil.format(DATA_PATH, getDbType().getDb(), dc.getDbPrefix()));
                dataSourceProperty.setInit(init);
            }
            dataSourceProperty.setSeata(databaseProperties.getIsSeata());
            dataSourceProperty.setDruid(properties.getDruid());
            if (isNotErrorRetry) {
                // 链接错误重试次数
                dataSourceProperty.getDruid().setConnectionErrorRetryAttempts(0);
                // 获取失败后中断
                dataSourceProperty.getDruid().setBreakAfterAcquireFailure(true);
            }
            putDs(dataSourceProperty);
        });
        return true;
    }

    private boolean addSystem(Long tenantId, boolean isInitSchema, boolean isNotErrorRetry) {
        DataSourceProperty defDataSourceProperty = properties.getDatasource().get(ContextConstants.DEF_TENANT_ID_STR);
        ArgumentAssert.notNull(defDataSourceProperty, "请先配置默认[{}]数据源", ContextConstants.DEF_TENANT_ID_STR);

        // 读取lamp.database.initDatabasePrefix 配置的租户前缀，动态初始化数据库
        databaseProperties.getInitDatabasePrefix().forEach(database -> {
            // 在程序启动时配置的默认库 数据源配置的基础上，修改租户库自己的特殊配置
            DataSourceProperty newDataSourceProperty = BeanUtil.toBean(defDataSourceProperty, DataSourceProperty.class);
            newDataSourceProperty.setPoolName(DsThreadProcessor.getPoolName(database, String.valueOf(tenantId)));
            if (DbType.ORACLE.getDb().equals(getDbType().getDb())) {
                newDataSourceProperty.setUsername(newDataSourceProperty.getPoolName());
                newDataSourceProperty.setPassword(newDataSourceProperty.getPoolName());
            } else {
                String oldDatabase = DbPlusUtil.getDataBaseNameByUrl(defDataSourceProperty.getUrl());
                String newDatabase = StrUtil.join(StrUtil.UNDERLINE, database, tenantId);
                newDataSourceProperty.setUrl(StrUtil.replace(defDataSourceProperty.getUrl(), oldDatabase, newDatabase));
            }
            if (isInitSchema) {
                DatasourceInitProperties init = newDataSourceProperty.getInit();
                if (init == null) {
                    init = new DatasourceInitProperties();
                }
                // 待创建的表结构
                init.setSchema(StrUtil.format(SCHEMA_PATH, getDbType().getDb(), database));
                // 待初始化的数据
                init.setData(StrUtil.format(DATA_PATH, getDbType().getDb(), database));
                newDataSourceProperty.setInit(init);
            }
            newDataSourceProperty.setSeata(databaseProperties.getIsSeata());
            newDataSourceProperty.setDruid(properties.getDruid());
            if (isNotErrorRetry) {
                // 链接错误重试次数
                newDataSourceProperty.getDruid().setConnectionErrorRetryAttempts(0);
                // 获取失败后中断
                newDataSourceProperty.getDruid().setBreakAfterAcquireFailure(true);
            }

            // 动态新增数据源
            putDs(newDataSourceProperty);
        });
        return true;
    }

    private Set<String> putDs(DataSourceProperty dsp) {
        try {
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) this.dataSource;
            DataSource newDataSource = defaultDataSourceCreator.createDataSource(dsp);
            ds.addDataSource(dsp.getPoolName(), newDataSource);
            return ds.getDataSources().keySet();
        } catch (ErrorCreateDataSourceException e) {
            log.error("数据源初始化期间出现异常", e);
            throw new BizException("数据源初始化期间出现异常", e);
        }
    }

}
