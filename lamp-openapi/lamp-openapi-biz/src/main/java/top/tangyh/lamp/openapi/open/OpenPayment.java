package top.tangyh.lamp.openapi.open;

import com.gitee.sop.support.annotation.Open;
import com.gitee.sop.support.context.OpenContext;
import top.tangyh.lamp.openapi.open.req.PayOrderSearchRequest;
import top.tangyh.lamp.openapi.open.req.PayTradeWapPayRequest;
import top.tangyh.lamp.openapi.open.req.SaveBaseEmployeeRequest;
import top.tangyh.lamp.openapi.open.resp.GetBaseEmployeeResponse;
import top.tangyh.lamp.openapi.open.resp.PayOrderSearchResponse;
import top.tangyh.lamp.openapi.open.resp.PayTradeWapPayResponse;

/**
 * 演示 - 支付接口
 *
 * @author zuihou
 */
public interface OpenPayment {

    /**
     * 手机网站支付接口
     *
     * @apiNote 该接口是页面跳转接口，用于生成用户访问跳转链接。
     * 请在服务端执行SDK中pageExecute方法，读取响应中的body()结果。
     * 该结果用于跳转到页面，返回到用户浏览器渲染或重定向跳转到页面。
     * 具体使用方法请参考 <a href="https://torna.cn" target="_blank">接入指南</a>
     *
     *
     * @param request 请求参数
     * @param context 上下文参数
     */
    @Open("openapi.wap.pay")
    PayTradeWapPayResponse tradeWapPay(PayTradeWapPayRequest request, OpenContext context);


    /**
     * 订单查询接口
     *
     * @param request
     * @return
     */
    @Open(value = "openapi.order.search", permission = true)
    PayOrderSearchResponse orderSearch(PayOrderSearchRequest request);

    @Open(value = "openapi.employee.save", permission = false)
    GetBaseEmployeeResponse saveOrder(SaveBaseEmployeeRequest request);

}
