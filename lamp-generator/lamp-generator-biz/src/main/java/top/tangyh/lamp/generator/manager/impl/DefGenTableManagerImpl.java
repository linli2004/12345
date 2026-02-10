package top.tangyh.lamp.generator.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.druid.DruidConfig;
import com.baomidou.dynamic.datasource.exception.ErrorCreateDataSourceException;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.annotation.DbType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.basic.exception.BizException;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.basic.utils.DbPlusUtil;
import top.tangyh.lamp.generator.entity.DefGenTable;
import top.tangyh.lamp.generator.manager.DefGenTableManager;
import top.tangyh.lamp.generator.mapper.DefGenTableMapper;
import top.tangyh.lamp.generator.mapper.GenDefDatasourceConfigMapper;
import top.tangyh.lamp.system.entity.tenant.DefDatasourceConfig;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * 通用业务实现类
 * 代码生成
 * </p>
 *
 * @author zuihou
 * @date 2022-03-01
 * @create [2022-03-01] [zuihou] [代码生成器生成]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefGenTableManagerImpl extends SuperManagerImpl<DefGenTableMapper, DefGenTable> implements DefGenTableManager {

    private final GenDefDatasourceConfigMapper defDatasourceConfigMapper;
    private final DefaultDataSourceCreator druidDataSourceCreator;
    private final DynamicDataSourceProperties properties;
    private final DataSource dataSource;
    private final Map<String, DataSource> dsMap = new HashMap<>();

    @Override
    public DbType getDbType() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource primary = ds.getDataSource(null);
        return DbPlusUtil.getDbType(primary);
    }

    @Override
    public DataSource getDs(Long dsId) {
        ArgumentAssert.notNull(dsId, "请先选择数据源");
        DefDatasourceConfig defDatasourceConfig = defDatasourceConfigMapper.selectById(dsId);
        ArgumentAssert.notNull(defDatasourceConfig, "请先配置数据源:{}", dsId);

        String key = defDatasourceConfig.getUrl() + defDatasourceConfig.getDriverClassName() + defDatasourceConfig.getUsername() + defDatasourceConfig.getPassword();
        if (dsMap.containsKey(key)) {
            return dsMap.get(key);
        }

        ArgumentAssert.notNull(defDatasourceConfig, "请先配置数据源:{}", dsId);
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setUrl(defDatasourceConfig.getUrl());
        dataSourceProperty.setUsername(defDatasourceConfig.getUsername());
        dataSourceProperty.setPassword(defDatasourceConfig.getPassword());
        dataSourceProperty.setDriverClassName(defDatasourceConfig.getDriverClassName());
        DataSource ds = getDs(dataSourceProperty);
        dsMap.put(key, ds);
        return ds;
    }


    public DataSource getDs(DataSourceProperty dataSourceProperty) {
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
        Properties pro = new Properties();
        pro.setProperty("remarks", "true");
        pro.setProperty("useInformationSchema", "true");
        dataSourceProperty.getDruid().setConnectionProperties(pro);

        try {
            return druidDataSourceCreator.createDataSource(dataSourceProperty);
        } catch (ErrorCreateDataSourceException e) {
            log.error("数据源初始化期间出现异常", e);
            throw new BizException("数据源初始化期间出现异常", e);
        } catch (Exception e) {
            log.error("创建测试链接错误 {}", dataSourceProperty.getUrl());
            throw new BizException("创建测试链接错误 " + dataSourceProperty.getUrl(), e);
        }
    }
}
