package top.tangyh.lamp;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

/**
 * @author zuihou
 * @date 2018-01-14 11:11
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
@Slf4j
public class MonitorServerApplication {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext application = SpringApplication.run(MonitorServerApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("""        
                        
                        ----------------------------------------------------------
                        应用 {} 启动成功!
                        监控地址:  http://{}:{}/{}
                        ----------------------------------------------------------
                        """,
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port", "8080"),
                env.getProperty("spring.application.name"));

    }
}
