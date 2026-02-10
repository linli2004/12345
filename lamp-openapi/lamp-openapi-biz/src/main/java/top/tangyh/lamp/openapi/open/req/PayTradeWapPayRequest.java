package top.tangyh.lamp.openapi.open.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

/**
 * alipay.trade.wap.pay(手机网站支付接口)
 *
 * @author zuihou
 * https://opendocs.alipay.com/open/29ae8cb6_alipay.trade.wap.pay?pathHash=1ef587fd&ref=api&scene=21
 */
@Data
public class PayTradeWapPayRequest {


    /**
     * 商户网站唯一订单号
     *
     * @mock 70501111111S001111119
     */
    @Length(max = 64)
    @NotBlank(message = "商户网站唯一订单号必填")
    private String outTradeNo;

    /**
     * 订单总金额.单位为元，精确到小数点后两位，取值范围：[0.01,100000000]
     *
     * @mock 9.00
     */
    @NotNull(message = "订单总金额不能为空")
    private BigDecimal totalAmount;


    /**
     * 订单标题。注意：不可使用特殊字符，如 /，=，& 等。
     *
     * @mock 大乐透
     */
    @Length(max = 256)
    @NotBlank(message = "订单标题不能为空")
    private String subject;

    /**
     * 销售产品码，商家和支付平台签约的产品码。手机网站支付为：QUICK_WAP_WAY
     *
     * @mock QUICK_WAP_WAY
     */
    @NotBlank(message = "销售产品码不能为空")
    @Length(max = 64)
    private String productCode;


    /**
     * 针对用户授权接口，获取用户相关数据时，用于标识用户授权关系
     *
     * @mock appopenBb64d181d0146481ab6a762c00714cC27
     */
    @Length(max = 40)
    private String authToken;

    /**
     * 用户付款中途退出返回商户网站的地址
     *
     * @mock http://www.taobao.com/product/113714.html
     */
    @Length(max = 400)
    private String quit_url;

    /**
     * 订单包含的商品列表信息，json格式，其它说明详见商品明细说明
     */
    private List<GoodsDetail> goodsDetail;

    /**
     * 绝对超时时间，格式为yyyy-MM-dd HH:mm:ss。超时时间范围：1m~15d。
     *
     * @mock 2016-12-31 10:05:00
     */
    @Length(max = 32)
    private String timeExpire;

    /**
     * 商户传入业务信息，具体值要和支付平台约定，应用于安全，营销等参数直传场景，格式为json格式
     *
     * @mock {"mc_create_trade_ip":"127.0.0.1"}
     */
    @Length(max = 512)
    private String businessParams;

    /**
     * 公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数。支付平台只会在同步返回（包括跳转回商户网站）和异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付平台。
     *
     * @mock merchantBizType%3d3C%26merchantBizNo%3d2016010101111
     */
    @Length(max = 512)
    private String passbackParams;

    /**
     * 商户原始订单号，最大长度限制32位
     *
     * @mock {"mc_create_trade_ip":"127.0.0.1"}
     */
    @Length(max = 32)
    private String merchantOrderNo;

    // ---

    @Data
    public static class GoodsDetail {
        /**
         * 商品的编号
         *
         * @mock apple-01
         */
        @NotBlank
        @Length(max = 64)
        private String goodsId;


        /**
         * 商品名称
         *
         * @mock ipad
         */
        @NotBlank
        @Length(max = 256)
        private String goodsName;

        /**
         * 商品数量
         *
         * @mock 1
         */
        @NotNull
        private Integer quantity;

        /**
         * 商品单价，单位为元
         *
         * @mock 2000
         */
        @NotNull
        private BigDecimal price;

        /**
         * 支付平台定义的统一商品编号
         *
         * @mock 20010001
         */
        @Length(max = 32)
        private String alipayGoodsId;

        /**
         * 商品类目
         *
         * @mock 34543238
         */
        @Length(max = 24)
        private String goodsCategory;

        /**
         * 商品类目树，从商品类目根节点到叶子节点的类目id组成，类目id值使用|分割
         *
         * @mock 124868003|126232002|126252004
         */
        @Length(max = 128)
        private String categoriesTree;

        /**
         * 商品描述信息
         *
         * @mock 特价手机
         */
        @Length(max = 1000)
        private String body;

        /**
         * 商品的展示地址
         *
         * @mock http://www.alipay.com/xxx.jpg
         */
        @Length(max = 400)
        private String showUrl;


    }

}
