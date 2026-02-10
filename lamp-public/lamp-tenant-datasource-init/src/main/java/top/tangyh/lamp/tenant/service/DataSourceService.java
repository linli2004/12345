package top.tangyh.lamp.tenant.service;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;

import java.util.Set;

/**
 * 数据源管理
 *
 * @author zuihou
 * @date 2020年03月15日11:31:57
 */
public interface DataSourceService {
    /**
     * 查询所有的数据源
     *
     * @return
     */
    Set<String> findAll();

    /**
     * 测试 数据源是否可以链接
     *
     * @param dataSourceProperty
     * @return
     */
    boolean testConnection(DataSourceProperty dataSourceProperty);


    /**
     * 删除数据库和数据源
     *
     * @param tenantId
     * @return
     */
    boolean removeDbAndDs(Long tenantId);


    /**
     * 检查指定租户的数据源是否链接正常
     *
     * @param tenantId 租户ID
     * @return
     */
    boolean check(Long tenantId);

    /**
     * 创建数据库
     *
     * @param tenantId
     */
    void createDatabase(Long tenantId);

    /**
     * 其他服务 依次初始化 数据源链接
     * <p>
     * 按yml中配置，不执行脚本，创建数据源，失败不需要重试
     *
     * @param tenantId 租户id
     * @return
     */
    boolean initDataSource(Long tenantId);

    /**
     * 启动时加载系统Druid数据源 失败时重试
     * <p>
     * 按yml中配置，不执行脚本，创建数据源， 失败需要重试
     *
     * @return
     */
    boolean loadSystemDataSource();

    /**
     * 启动时加载自定义Druid数据源 失败时重试
     * <p>
     * 按yml中配置，不执行脚本，创建数据源， 失败需要重试
     *
     * @return
     */
    boolean loadCustomDataSource();

    /**
     * 批量添加自定义数据源 和 脚本数据
     * <p>
     * 按yml中配置，执行脚本，创建数据源，失败不需要重试
     *
     * @param tenantId 租户id
     * @return
     */
    boolean addCustomDsAndData(Long tenantId);

    /**
     * 批量添加系统数据源 和 脚本数据
     * <p>
     * 按yml中配置，执行脚本，创建数据源，失败不需要重试
     *
     * @param tenantId 租户id
     * @return
     */
    boolean addSystemDsAndData(Long tenantId);

}
