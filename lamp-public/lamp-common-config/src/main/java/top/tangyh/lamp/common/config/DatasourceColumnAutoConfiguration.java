package top.tangyh.lamp.common.config;

import com.baomidou.dynamic.datasource.processor.DsJakartaSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.tangyh.basic.constant.Constants;
import top.tangyh.basic.database.plugins.TenantLineAnnotationRegister;
import top.tangyh.basic.database.properties.DatabaseProperties;
import top.tangyh.lamp.tenant.context.InitDatabaseOnStarted;
import top.tangyh.lamp.tenant.dynamic.processor.DsThreadProcessor;
import top.tangyh.lamp.tenant.service.DatasourceInitDataSourceService;

/**
 * lamp.database.multiTenantType = DATASOURCE，DATASOURCE_COLUMN 时，该类启用.
 * 此时，项目的多租户模式切换成：动态切换数据源模式。
 * <p>
 * 即：每个租户链接独立的一个数据源，每个请求的请求头中需要携带的租户编码，在每个服务的拦截器(TenantContextHandlerInterceptor)中,将租户编码封装到 当前线程变量（ThreadLocal），
 * 在mybatis 执行sql前，通过 DsThreadProcessor 类获取到ThreadLocal中的租户编码，动态切换数据源
 * <p>
 *
 * @author tangyh
 * @date 2022年04月02日20:34:03
 */

@Configuration
public class DatasourceColumnAutoConfiguration {

    /**
     * 项目启动时，初始化数据源
     */
    @Bean
    public InitDatabaseOnStarted getInitDatabaseOnStarted(DatasourceInitDataSourceService initSystemContext) {
        return new InitDatabaseOnStarted(initSystemContext);
    }

    /**
     * DATASOURCE 模式 自定义数据源处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public DsProcessor dsProcessor() {
        // 当前线程 获取数据源
        DsThreadProcessor threadProcessor = new DsThreadProcessor();
        // 当前session 获取数据源
        DsJakartaSessionProcessor sessionProcessor = new DsJakartaSessionProcessor();
        // sp el 表达式 获取数据源
        DsSpelExpressionProcessor dsSpelExpressionProcessor = new DsSpelExpressionProcessor();
        threadProcessor.setNextProcessor(sessionProcessor);
        sessionProcessor.setNextProcessor(dsSpelExpressionProcessor);
        return threadProcessor;
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression("'DATASOURCE_COLUMN'.equals('${" + DatabaseProperties.PREFIX + ".multiTenantType}') && ${" + Constants.PROJECT_PREFIX + ".webmvc.undertow:false}")
    public TenantLineAnnotationRegister getTenantLineAnnotationRegister() {
        return new TenantLineAnnotationRegister();
    }

}
