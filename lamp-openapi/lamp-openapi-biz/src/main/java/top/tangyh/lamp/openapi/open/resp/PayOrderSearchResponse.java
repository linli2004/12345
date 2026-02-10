package top.tangyh.lamp.openapi.open.resp;

import lombok.Data;

/**
 * @author 六如
 */
@Data
public class PayOrderSearchResponse {

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 支付编号
     */
    private String payNo;

    /**
     * 支付人id
     */
    private Long payUserId;

    /**
     * 支付人姓名
     */
    private String payUserName;

    /**
     * 备注
     */
    private String remark;
}
