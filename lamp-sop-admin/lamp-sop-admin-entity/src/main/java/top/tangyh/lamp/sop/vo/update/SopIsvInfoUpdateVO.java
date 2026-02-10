package top.tangyh.lamp.sop.vo.update;

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
import top.tangyh.basic.base.entity.SuperEntity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 表单修改方法VO
 * isv信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-11 10:51:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "isv信息表")
public class SopIsvInfoUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @NotNull(message = "请填写id", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 状态
     * [1-启用 2-禁用]
     */
    @Schema(description = "状态")
    @NotNull(message = "请填写状态")
    private Integer status;
    /**
     * 备注
     */
    @Schema(description = "备注")
    @Size(max = 512, message = "备注长度不能超过{max}")
    private String remark;
    /**
     * 开始有效期
     */
    @Schema(description = "开始有效期")
    private LocalDateTime startExpirationTime;
    /**
     * 结束有效期
     */
    @Schema(description = "结束有效期")
    private LocalDateTime endExpirationTime;
    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotEmpty(message = "请填写名称")
    @Size(max = 255, message = "名称长度不能超过{max}")
    private String name;
    /**
     * 回调接口
     */
    @Schema(description = "回调接口")
    @Size(max = 255, message = "回调接口长度不能超过{max}")
    private String notifyUrl;

}
