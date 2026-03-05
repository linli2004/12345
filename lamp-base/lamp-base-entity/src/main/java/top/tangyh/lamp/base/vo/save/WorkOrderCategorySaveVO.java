package top.tangyh.lamp.base.vo.save;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 表单保存方法VO
 * 工单分类
 * </p>
 *
 * @author lunar
 * @date 2026-02-27 09:33:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "工单分类")
public class WorkOrderCategorySaveVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotEmpty(message = "请填写名称")
    @Size(max = 255, message = "名称长度不能超过{max}")
    private String name;
    /**
     * 树层级
     */
    @Schema(description = "树层级")
    private Integer treeGrade;
    /**
     * 状态;[0-禁用 1-启用]
     */
    @Schema(description = "状态")
    private Boolean state;
    /**
     * 备注
     */
    @Schema(description = "备注")
    @Size(max = 255, message = "备注长度不能超过{max}")
    private String remarks;
    /**
     * 创建人组织
     */
    @Schema(description = "创建人组织")
    private Long createdOrgId;

    @Schema(description = "父节点")
    protected Long parentId;

    @Schema(description = "排序号")
    protected Integer sortValue;


}
