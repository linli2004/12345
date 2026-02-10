package top.tangyh.lamp.sop.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.tangyh.basic.interfaces.BaseEnum;


/**
 * 通知状态
 * 1-发送成功,2-发送失败,3-重试结束
 */
@AllArgsConstructor
@Getter
@Schema(description = "通知状态-枚举")
public enum NotifyStatusEnum implements BaseEnum {

    WAIT_SEND("0", "待发送"),
    SEND_SUCCESS("1", "发送成功"),
    SEND_FAIL("2", "发送失败"),
    RETRY_OVER("3", "重试结束"),
    END("4", "手动结束");
    @Schema(description = "状态")
    private final String code;
    @Schema(description = "描述")
    private final String desc;

}
