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
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("sop_sys_config")
public class SopSysConfig extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置key
     */
    @TableField(value = "config_key", condition = LIKE)
    private String configKey;
    /**
     * 配置值
     */
    @TableField(value = "config_value", condition = LIKE)
    private String configValue;
    /**
     * 备注
     */
    @TableField(value = "remark", condition = LIKE)
    private String remark;
    /**
     * 是否删除
     */
    @TableField(value = "deleted_at", condition = EQUAL)
    private Long deletedAt;
}
