# 推送文档流程

- 编译 util

```
mvn clean install 
```

- 编译 lamp-datasource-max

```
mvn clean install 
```

- 修改配置文件
- 执行推送

```
mvn -Dfile.encoding=UTF-8 smart-doc:torna-rpc -pl :lamp-openapi-server -am -f pom.xml
```

- 登录torna后台查看文档

# 同步文档至sop

- 启动 torna
- 启动lamp-sop-admin
- 在页面上点击同步
