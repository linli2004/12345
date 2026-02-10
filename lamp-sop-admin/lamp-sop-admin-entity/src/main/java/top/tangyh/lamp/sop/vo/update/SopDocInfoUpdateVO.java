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
 * 文档信息
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
@Schema(description = "文档信息")
public class SopDocInfoUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @NotNull(message = "请填写id", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 应用id
     * doc_app.id
     */
    @Schema(description = "应用id")
    @NotNull(message = "请填写应用id")
    private Long docAppId;
    /**
     * 文档标题
     */
    @Schema(description = "文档标题")
    @NotEmpty(message = "请填写文档标题")
    @Size(max = 128, message = "文档标题长度不能超过{max}")
    private String docTitle;
    /**
     * 文档id
     * torna.doc_info.id
     */
    @Schema(description = "文档id")
    @NotNull(message = "请填写文档id")
    private Long docId;
    /**
     * 文档编码
     */
    @Schema(description = "文档编码")
    @Size(max = 64, message = "文档编码长度不能超过{max}")
    private String docCode;
    /**
     * 文档类型
     * [1-dubbo 2-富文本 3-Markdown]
     */
    @Schema(description = "文档类型")
    private Integer docType;
    /**
     * 来源类型
     * [1-torna 2-自建]
     */
    @Schema(description = "来源类型")
    private Integer sourceType;
    /**
     * 文档版本号
     */
    @Schema(description = "文档版本号")
    @NotEmpty(message = "请填写文档版本号")
    @Size(max = 16, message = "文档版本号长度不能超过{max}")
    private String docVersion;
    /**
     * 文档名称
     */
    @Schema(description = "文档名称")
    @NotEmpty(message = "请填写文档名称")
    @Size(max = 64, message = "文档名称长度不能超过{max}")
    private String docName;
    /**
     * 描述
     */
    @Schema(description = "描述")
    @NotEmpty(message = "请填写描述")
    @Size(max = 65535, message = "描述长度不能超过{max}")
    private String description;
    /**
     * 是否分类
     */
    @Schema(description = "是否分类")
    @NotNull(message = "请填写是否分类")
    private Integer isFolder;
    /**
     * 状态
     * [0-未发布 1-已发布]
     */
    @Schema(description = "状态")
    @NotNull(message = "请填写状态")
    private Integer isPublish;
    /**
     * 父文档节点id
     */
    @Schema(description = "父文档节点id")
    @NotNull(message = "请填写父文档节点id")
    private Long parentId;


}
