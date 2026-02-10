package com.gitee.sop.gateway.config;

import com.gitee.sop.gateway.service.ParamExecutor;
import com.gitee.sop.gateway.service.Serde;
import com.gitee.sop.gateway.service.impl.ParamExecutorImpl;
import com.gitee.sop.gateway.service.impl.SerdeGsonImpl;
import com.gitee.sop.gateway.service.impl.SerdeImpl;
import com.gitee.sop.gateway.service.manager.ApiManager;
import com.gitee.sop.gateway.service.manager.IsvApiPermissionManager;
import com.gitee.sop.gateway.service.manager.IsvManager;
import com.gitee.sop.gateway.service.manager.SecretManager;
import com.gitee.sop.gateway.service.manager.impl.LocalApiManagerImpl;
import com.gitee.sop.gateway.service.manager.impl.LocalIsvApiPermissionManagerImpl;
import com.gitee.sop.gateway.service.manager.impl.LocalIsvManagerImpl;
import com.gitee.sop.gateway.service.manager.impl.LocalSecretManagerImpl;
import com.gitee.sop.gateway.service.manager.impl.RedisApiManagerImpl;
import com.gitee.sop.gateway.service.manager.impl.RedisIsvApiPermissionManagerImpl;
import com.gitee.sop.gateway.service.manager.impl.RedisIsvManagerImpl;
import com.gitee.sop.gateway.service.manager.impl.RedisSecretManager;
import com.gitee.sop.support.message.OpenMessageFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author 六如
 */
@Configuration
@Slf4j
public class GatewayConfig {

    private static final String REDIS = "redis";

    @Bean
    public ApiManager localApiManager(@Value("${gateway.manager.api:local}") String gatewayManagerApi) {
        if (REDIS.equalsIgnoreCase(gatewayManagerApi)) {
            return new RedisApiManagerImpl();
        }
        return new LocalApiManagerImpl();
    }


    @Bean
    public IsvManager localIsvManager(@Value("${gateway.manager.isv:local}") String gatewayManagerIsv) {
        if (REDIS.equalsIgnoreCase(gatewayManagerIsv)) {
            return new RedisIsvManagerImpl();
        }
        return new LocalIsvManagerImpl();
    }

    @Bean
    public SecretManager localSecretManager(@Value("${gateway.manager.secret:local}") String gatewayManagerSecret) {
        if (REDIS.equalsIgnoreCase(gatewayManagerSecret)) {
            return new RedisSecretManager();
        }
        return new LocalSecretManagerImpl();
    }

    @Bean
    public IsvApiPermissionManager localIsvApiPermissionManager(
            @Value("${gateway.manager.isv-api-perm:local}") String gatewayManagerIsvApiPrm
    ) {
        if (REDIS.equalsIgnoreCase(gatewayManagerIsvApiPrm)) {
            return new RedisIsvApiPermissionManagerImpl();
        }
        return new LocalIsvApiPermissionManagerImpl();
    }


    // 默认使用fastjson2序列化
    @Bean
    public Serde serdeFastjson2(@Value("${gateway.serialize.json-formatter:fastjson2}") String gatewaySerializeJsonFormatter) {
        log.info("[init]使用{}序列化", gatewaySerializeJsonFormatter);
        if ("gson".equalsIgnoreCase(gatewaySerializeJsonFormatter)) {
            return new SerdeGsonImpl();
        }
        return new SerdeImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ParamExecutor paramExecutor() {
        return new ParamExecutorImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public Serde serde() {
        return new SerdeImpl();
    }

    @PostConstruct
    public void init() {
        OpenMessageFactory.initMessage();
    }
}
