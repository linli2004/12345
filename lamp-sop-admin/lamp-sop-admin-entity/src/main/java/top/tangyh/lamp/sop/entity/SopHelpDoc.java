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
import top.tangyh.basic.base.entity.TreeEntity;

import java.io.Serial;

import static com.baomidou.mybatisplus.annotation.SqlCondition.EQUAL;
import static top.tangyh.lamp.model.constant.Condition.LIKE;


/**
 * <p>
 * 实体类
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
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("sop_help_doc")
public class SopHelpDoc extends TreeEntity<SopHelpDoc, Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文档名称
     */
    @TableField(value = "label", condition = LIKE)
    private String label;

    /**
     * 状态
     * 1：启用，0：禁用
     */
    @TableField(value = "status", condition = EQUAL)
    private Boolean status;
    /**
     * 内容
     */
    @TableField(value = "content", condition = LIKE)
    private String content;
    /**
     * 内容类型
     * 1-Markdown,2-富文本
     */
    @TableField(value = "content_type", condition = EQUAL)
    private Integer contentType;
    /**
     * 父级id
     */
    @TableField(value = "parent_id", condition = EQUAL)
    private Long parentId;


}
