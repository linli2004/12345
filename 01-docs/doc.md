AbstractGlobalExceptionHandler
添加租户时，需要初始化网关

网关：
不能有：
lamp-validator-starter
spring-webmvc
不能使用构造器注入
所有的Feign必须延迟注入： @Lazy
lamp-base-server 不能加入spring-boot-starter-tomcat依赖，否则websocket冲突

必须有：lamp-log-start

## 单体版和微服务版合并测试

1. 代码生成
2. 远程调用
3. 