package top.tangyh.lamp.sop.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.Entity;

import java.io.Serial;

import static com.baomidou.mybatisplus.annotation.SqlCondition.EQUAL;
import static top.tangyh.lamp.model.constant.Condition.LIKE;


/**
 * <p>
 * 实体类
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
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("sop_doc_info")
public class SopDocInfo extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 应用id
     * doc_app.id
     */
    @TableField(value = "doc_app_id", condition = EQUAL)
    private Long docAppId;
    /**
     * 文档标题
     */
    @TableField(value = "doc_title", condition = LIKE)
    private String docTitle;
    /**
     * 文档id
     * torna.doc_info.id
     */
    @TableField(value = "doc_id", condition = EQUAL)
    private Long docId;
    /**
     * 文档编码
     */
    @TableField(value = "doc_code", condition = LIKE)
    private String docCode;
    /**
     * 文档类型
     * [1-dubbo 2-富文本 3-Markdown]
     */
    @TableField(value = "doc_type", condition = EQUAL)
    private Integer docType;
    /**
     * 来源类型
     * [1-torna 2-自建]
     */
    @TableField(value = "source_type", condition = EQUAL)
    private Integer sourceType;
    /**
     * 文档版本号
     */
    @TableField(value = "doc_version", condition = LIKE)
    private String docVersion;
    /**
     * 文档名称
     */
    @TableField(value = "doc_name", condition = LIKE)
    private String docName;
    /**
     * 描述
     */
    @TableField(value = "description", condition = LIKE)
    private String description;
    /**
     * 是否分类
     */
    @TableField(value = "is_folder", condition = EQUAL)
    private Integer isFolder;
    /**
     * 状态
     * [0-未发布 1-已发布]
     */
    @TableField(value = "is_publish", condition = EQUAL)
    private Integer isPublish;
    /**
     * 父文档节点id
     */
    @TableField(value = "parent_id", condition = EQUAL)
    private Long parentId;


}
