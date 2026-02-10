package com.gitee.sop.gateway.config;

import com.gitee.sop.support.dto.ApiConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 六如
 */
@Configuration
@ConfigurationProperties(prefix = "api")
public class GateApiConfig extends ApiConfig {
}
