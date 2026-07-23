package top.tangyh.lamp.base.vo.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;


/**
 * <p>
 * 表单查询条件VO
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
public class RemindTemplatePageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;
    /**
     * 展示内容
     */
    @Schema(description = "展示内容")
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
    private String fileName;


}
