package top.tangyh.lamp.sop.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import top.tangyh.basic.constant.Constants;

/**
 * 通知配置
 * @author tangyh
 * @since 2025/12/17 15:49
 */
@Setter
@Getter
@RefreshScope
@ConfigurationProperties(prefix = NotifyProperties.PREFIX)
public class NotifyProperties {
    public static final String PREFIX = Constants.PROJECT_PREFIX + ".notify";

    /**
     * 重试间隔
     * 对应第1，2，3...次尝试
     *      即1分钟后进行第一次尝试，如果失败，5分钟后进行第二次尝试
     */
    private String timeLevel = "1m,5m,10m,30m,1h,2h,5h";
    /**
     * 重试线程数
     */
    private Integer threads = 2;
    /**
     * 每个线程跑几个任务
     */
    private Integer threadTasks = 50;
}
