package top.tangyh.lamp.sop.vo.save;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author 六如
 */
@Data
public class IsvKeysGenVO {
    /**
     * 秘钥格式，1：PKCS8(JAVA适用)，2：PKCS1(非JAVA适用)
     */
    @Min(value = 1, message = "秘钥格式错误")
    @Max(value = 2, message = "秘钥格式错误")
    private Integer keyFormat;
}
