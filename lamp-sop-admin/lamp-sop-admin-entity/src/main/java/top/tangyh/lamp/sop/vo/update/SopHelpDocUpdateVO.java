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
 * 帮助内容
 * </p>
 *
 * @author zuihou
 * @date 2025-12-18 12:21:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "帮助内容表")
public class SopHelpDocUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @NotNull(message = "请填写id", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 文档名称
     */
    @Schema(description = "文档名称")
    @NotEmpty(message = "请填写文档名称")
    @Size(max = 64, message = "文档名称长度不能超过{max}")
    private String label;
    /**
     * 排序
     */
    @Schema(description = "排序")
    @NotNull(message = "请填写排序")
    private Integer sortValue;
    /**
     * 状态
     * 1：启用，0：禁用
     */
    @Schema(description = "状态")
    @NotNull(message = "请填写状态")
    private Boolean status;
    /**
     * 内容
     */
    @Schema(description = "内容")
    @Size(max = 2147483647, message = "内容长度不能超过{max}")
    private String content;
    /**
     * 内容类型
     * 1-Markdown,2-富文本
     */
    @Schema(description = "内容类型")
    @NotNull(message = "请填写内容类型")
    private Integer contentType;
    /**
     * 父级id
     */
    @Schema(description = "父级id")
    @NotNull(message = "请填写父级id")
    private Long parentId;


}
