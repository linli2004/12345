package top.tangyh.lamp.sdk.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.tangyh.lamp.sdk.request.PayTradeWapPayRequest;
import top.tangyh.lamp.sdk.response.PayTradeWapPayResponse;
import top.tangyh.lamp.sdkcore.param.BaseParam;

/**
 * pay.trade.wap.pay(手机网站支付接口)
 *
 * @author 六如
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PayTradeWapPayParam extends BaseParam<PayTradeWapPayRequest, PayTradeWapPayResponse> {
    @Override
    protected String method() {
        return "openapi.wap.pay";
    }
}
