package top.tangyh.lamp.sop.vo.save;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 表单保存方法VO
 * ISV秘钥管理
 * </p>
 *
 * @author zuihou
 * @since 2025-05-11 10:34:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "ISV秘钥管理")
public class SopIsvKeysSaveVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ISV
     * isv_info.id
     */
    @Schema(description = "ISV")
    @NotNull(message = "请填写ISV")
    private Long isvId;
    /**
     * 秘钥格式
     * [1-PKCS8(JAVA适用) 2-PKCS1(非JAVA适用)]
     */
    @Schema(description = "秘钥格式")
    @NotNull(message = "请填写秘钥格式")
    private Integer keyFormat;
    /**
     * 开发者生成的公钥
     */
    @Schema(description = "开发者生成的公钥")
    @NotEmpty(message = "请填写开发者生成的公钥")
    @Size(max = 65535, message = "开发者生成的公钥长度不能超过{max}")
    private String publicKeyIsv;
    /**
     * 开发者生成的私钥
     * （提供给开发者）
     */
    @Schema(description = "开发者生成的私钥")
    @NotEmpty(message = "请填写开发者生成的私钥")
    @Size(max = 65535, message = "开发者生成的私钥长度不能超过{max}")
    private String privateKeyIsv;
    /**
     * 平台生成的公钥
     * （提供给开发者）
     */
    @Schema(description = "平台生成的公钥")
    @NotEmpty(message = "请填写平台生成的公钥")
    @Size(max = 65535, message = "平台生成的公钥长度不能超过{max}")
    private String publicKeyPlatform;
    /**
     * 平台生成的私钥
     */
    @Schema(description = "平台生成的私钥")
    @NotEmpty(message = "请填写平台生成的私钥")
    @Size(max = 65535, message = "平台生成的私钥长度不能超过{max}")
    private String privateKeyPlatform;


}
