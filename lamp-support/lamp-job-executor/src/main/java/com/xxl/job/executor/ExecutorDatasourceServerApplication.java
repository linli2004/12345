package com.xxl.job.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import top.tangyh.basic.validator.annotation.EnableFormValidator;

import java.net.UnknownHostException;

import static top.tangyh.lamp.common.constant.BizConstant.BUSINESS_PACKAGE;
import static top.tangyh.lamp.common.constant.BizConstant.UTIL_PACKAGE;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication
@ComponentScan({
        UTIL_PACKAGE, BUSINESS_PACKAGE, "com.xxl.job.executor"
})
@EnableFormValidator
//@EnableFeignClients({
//        BUSINESS_PACKAGE
//})
@Slf4j
public class ExecutorDatasourceServerApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(ExecutorDatasourceServerApplication.class, args);
        Environment env = application.getEnvironment();
        String msg = """
                
                ----------------------------------------------------------
                应用 '{}' 启动成功， JDK版本号：{} ！
                xxl-job 调度器地址:   http://127.0.0.1:8767/xxl-job-admin
                数据库监控（可用于排查数据源是否链接成功）:   http://{}:{}/druid
                当前环境变量：{} 日志路径：{}
                ----------------------------------------------------------
                """;

        log.info(msg,
                env.getProperty("spring.application.name"),
                env.getProperty("java.version"),
                "127.0.0.1",
                env.getProperty("server.port"),
                env.getProperty("spring.profiles.active"), env.getProperty("LOG_PATH")
        );
    }

}
