package top.tangyh.lamp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.tangyh.basic.validator.annotation.EnableFormValidator;
import top.tangyh.lamp.common.ServerApplication;

import java.net.UnknownHostException;

/**
 * SOP管理端启动类
 *
 * @author zuihou
 * @since 2025-05-07 11:15:23
 */
@SpringBootApplication
@EnableDiscoveryClient
@Configuration
@EnableFeignClients(value = {"top.tangyh.lamp", "top.tangyh.basic"})
@ComponentScan(basePackages = {"top.tangyh.lamp", "top.tangyh.basic"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@Slf4j
@EnableFormValidator
@EnableScheduling
public class SopAdminServerApplication extends ServerApplication {
    public static void main(String[] args) throws UnknownHostException {
        start(SopAdminServerApplication.class, args);
    }
}
