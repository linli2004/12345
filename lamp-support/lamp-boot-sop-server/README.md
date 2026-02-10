# 注意事项

1. 若你在启动 此模块之前，启动过 lamp-openapi-server ，则有可能会报错

```
java.lang.RuntimeException: 接口[RegisterDTO(application=lamp-boot-sop-server-datasource, apiName=openapi.wap.pay, apiVersion=1.0, description=null, remark=null, interfaceClassName=top.tangyh.lamp.openapi.open.OpenPayment, methodName=tradeWapPay, paramInfo=[{"name":"request","type":"top.tangyh.lamp.openapi.open.req.PayTradeWapPayRequest"},{"name":"context","type":"com.gitee.sop.support.context.OpenContext"}], isPermission=0, isNeedToken=0, hasCommonResponse=1, apiMode=1)]已存在于[lamp-openapi-server]应用中.必须保证接口全局唯一
```

2. 此时你只需要删除 sop_api_info 表的数据即可解决

```
delete from sop_api_info;
```

3. 当然，你也可以注释代码： ApiRegisterServiceImpl

```java
private void check(ApiInfo apiInfo, RegisterDTO registerDTO) {
    if (!Objects.equals(apiInfo.getApplication(), registerDTO.getApplication())) {
        throw new RuntimeException("接口[" + registerDTO + "]已存在于[" + apiInfo.getApplication() + "]应用中.必须保证接口全局唯一");
    }
}
```