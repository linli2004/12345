package top.tangyh.lamp.common.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.database.config.BaseMybatisConfiguration;
import top.tangyh.basic.database.plugins.LampTenantLineInnerInterceptor;
import top.tangyh.basic.database.properties.DatabaseProperties;
import top.tangyh.basic.database.properties.MultiTenantType;
import top.tangyh.lamp.datascope.interceptor.DataScopeInnerInterceptor;

import java.util.ArrayList;
import java.util.List;

import static top.tangyh.lamp.common.constant.BizConstant.BUSINESS_PACKAGE;
import static top.tangyh.lamp.common.constant.BizConstant.UTIL_PACKAGE;

/**
 * lamp.database.multiTenantType = DATASOURCE 时，该类启用.
 * 此时，项目的多租户模式切换成：动态切换数据源模式。
 * <p>
 * 即：每个租户链接独立的一个数据源，每个请求的请求头中需要携带的租户编码，在每个服务的拦截器(TenantContextHandlerInterceptor)中,将租户编码封装到 当前线程变量（ThreadLocal），
 * 在mybatis 执行sql前，通过 DsThreadProcessor 类获取到ThreadLocal中的租户编码，动态切换数据源
 * <p>
 *
 * @author tangyh
 * @date 2022年04月02日20:34:03
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({DatabaseProperties.class})
@MapperScan(basePackages = {BUSINESS_PACKAGE, UTIL_PACKAGE}, annotationClass = Repository.class)
public class MybatisAutoConfiguration extends BaseMybatisConfiguration {
    public MybatisAutoConfiguration(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }

    @Override
    protected List<InnerInterceptor> getPaginationBeforeInnerInterceptor() {
        List<InnerInterceptor> list = new ArrayList<>();
        if (MultiTenantType.DATASOURCE_COLUMN.eq(databaseProperties.getMultiTenantType())) {
            log.info("检查到配置了:{}模式，已加载 column 部分插件", databaseProperties.getMultiTenantType());
            // COLUMN 模式 多租户插件
            LampTenantLineInnerInterceptor tli = new LampTenantLineInnerInterceptor();
            tli.setTenantLineHandler(new TenantLineHandler() {
                @Override
                public String getTenantIdColumn() {
                    return databaseProperties.getTenantIdColumn();
                }

                @Override
                public boolean ignoreTable(String tableName) {
                    boolean ignoreTable = databaseProperties.getIgnoreTable() != null && databaseProperties.getIgnoreTable().contains(tableName);

                    boolean ignoreTablePrefix = databaseProperties.getIgnoreTablePrefix() != null &&
                                                databaseProperties.getIgnoreTablePrefix().stream().anyMatch(prefix -> tableName.startsWith(prefix));
                    return ignoreTable || ignoreTablePrefix;
                }

                @Override
                public Expression getTenantId() {
                    return new LongValue(ContextUtil.getCurrentCompanyId());
                }
            });
            list.add(tli);
        }

        Boolean isDataScope = databaseProperties.getIsDataScope();
        if (isDataScope) {
            list.add(getDataScopeInnerInterceptor());
        }
        return list;
    }


    @Bean
    @Order(4)
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DatabaseProperties.PREFIX, name = "isDataScope", havingValue = "true")
    public DataScopeInnerInterceptor getDataScopeInnerInterceptor() {
        return new DataScopeInnerInterceptor();
    }

}
