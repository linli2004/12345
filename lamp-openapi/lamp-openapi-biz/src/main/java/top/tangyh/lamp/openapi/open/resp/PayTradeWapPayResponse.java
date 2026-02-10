package top.tangyh.lamp.openapi.open.resp;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @author 六如
 */
@Data
public class PayTradeWapPayResponse {

    /**
     * 用于跳转支付平台页面的信息，POST和GET方法生成内容不同：使用POST方法执行，结果为html form表单，在浏览器渲染即可<br>使用GET方法会得到支付平台URL，需要打开或重定向到该URL。建议使用POST方式。
     *
     * @mock 请参考响应示例
     */
    @NotNull
    private String pageRedirectionData;

}
