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
 * 文档应用
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
@TableName("sop_doc_app")
public class SopDocApp extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 应用名称
     */
    @TableField(value = "app_name", condition = LIKE)
    private String appName;
    /**
     * Torna应用token
     */
    @TableField(value = "token", condition = LIKE)
    private String token;
    /**
     * 是否发布
     */
    @TableField(value = "is_publish", condition = EQUAL)
    private Integer isPublish;


}
