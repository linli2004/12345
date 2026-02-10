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

/**
 * <p>
 * 表单修改方法VO
 * 系统配置表
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
@Schema(description = "系统配置表")
public class SopSysConfigUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @NotNull(message = "请填写主键id", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 配置key
     */
    @Schema(description = "配置key")
    @NotEmpty(message = "请填写配置key")
    @Size(max = 64, message = "配置key长度不能超过{max}")
    private String configKey;
    /**
     * 配置值
     */
    @Schema(description = "配置值")
    @NotEmpty(message = "请填写配置值")
    @Size(max = 256, message = "配置值长度不能超过{max}")
    private String configValue;
    /**
     * 备注
     */
    @Schema(description = "备注")
    @NotEmpty(message = "请填写备注")
    @Size(max = 128, message = "备注长度不能超过{max}")
    private String remark;

}
