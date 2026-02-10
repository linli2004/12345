package top.tangyh.lamp.base.vo.update.common;

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

import java.io.Serializable;

/**
 * <p>
 * 实体类
 * 个性参数
 * </p>
 *
 * @author zuihou
 * @since 2021-11-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(title = "BaseParameterUpdateVO", description = "个性参数")
public class BaseParameterUpdateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @NotNull(message = "请填写主键", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 参数键
     */
    @Schema(description = "参数键")
    @NotEmpty(message = "请填写参数键")
    @Size(max = 255, message = "参数键长度不能超过{max}")
    private String key;
    /**
     * 参数值
     */
    @Schema(description = "参数值")
    @NotEmpty(message = "请填写参数值")
    @Size(max = 255, message = "参数值长度不能超过{max}")
    private String value;
    /**
     * 参数名称
     */
    @Schema(description = "参数名称")
    @NotEmpty(message = "请填写参数名称")
    @Size(max = 255, message = "参数名称长度不能超过{max}")
    private String name;
    /**
     * 备注
     */
    @Schema(description = "备注")
    @Size(max = 255, message = "备注长度不能超过{max}")
    private String remarks;
    /**
     * 状态
     */
    @Schema(description = "状态")
    private Boolean state;

}
