package top.tangyh.lamp.model.enumeration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.tangyh.basic.interfaces.BaseEnum;

/**
 * 审核状态
 *[0-初始化 1-申请中 2-通过 99-退回]
 * @author tangyh
 * @since 2025/5/11 16:52
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Schema(description = "审核状态-枚举")
public enum AuditStatusEnum implements BaseEnum {
    INIT(0, "初始化", "default"),
    APPLYING(1, "申请中", "processing"),
    PASS(2, "通过", "success"),
    BACK(99, "退回", "error");

    private Integer val;
    private String desc;
    private String extra;

    @Override
    public String getCode() {
        return this.val.toString();
    }
}
