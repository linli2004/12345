package top.tangyh.lamp.base.vo.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.SuperEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 表单修改方法VO
 * 提醒模板
 * </p>
 *
 * @author lunar
 * @date 2026-03-10 08:39:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "提醒模板")
public class RemindTemplateUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @NotNull(message = "请填写ID", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotEmpty(message = "请填写名称")
    @Size(max = 255, message = "名称长度不能超过{max}")
    private String name;
    /**
     * 展示内容
     */
    @Schema(description = "展示内容")
    @Size(max = 65535, message = "展示内容长度不能超过{max}")
    private String displayContent;
    /**
     * 创建人组织
     */
    @Schema(description = "创建人组织")
    private Long createdOrgId;
    /**
     * 提醒类型
     */
    @Schema(description = "提醒类型")
    @Size(max = 255, message = "提醒类型长度不能超过{max}")
    private String type;
    /**
     * 附件id
     */
    @Schema(description = "附件id")
    private Long fileId;
    /**
     * 文件名称
     */
    @Schema(description = "文件名称")
    @Size(max = 255, message = "文件名称长度不能超过{max}")
    private String fileName;


}
