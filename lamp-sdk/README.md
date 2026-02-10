# 接口对应的sdk

sdk提供给ISV使用，可在此基础上进行封装，具体使用方式参见单元测试。

lamp-sdk 是完全独立的一个jar，是需要提供给第三方开发者使用的，所以lamp-sdk的依赖项尽可能的少，千万不要让lamp-sdk依赖lamp-column-max、lamp-datasource-max、lmap-util-max等模块了。

lamp-sdk 使用http工具，封装lamp-openapi模块已经开发好的 @Open 接口，用于提供给第三方调用，第三方通过lamp-sdk进行调用时，请求路径为： sop-gateway-server -> lamp-openapi，
sop-gateway-server负责对请求进行鉴权，lamp-openapi负责接口的业务逻辑。

- lamp-sdk-core
  sdk 基础代码

- lamp-simple-sdk
  提供给第三方的sdk