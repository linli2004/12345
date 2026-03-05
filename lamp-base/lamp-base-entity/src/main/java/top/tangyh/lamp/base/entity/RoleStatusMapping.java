package top.tangyh.lamp.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.Entity;

import java.io.Serial;

import static top.tangyh.lamp.model.constant.Condition.LIKE;


/**
 * <p>
 * 实体类
 * 角色视图映射配置表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:49:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("role_status_mapping")
public class RoleStatusMapping extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 业务节点编码
     */
    @TableField(value = "node_code", condition = LIKE)
    private String nodeCode;
    /**
     * 角色编码:1-镇级网格专员;2-基层部门分管领导;3-基层部门负责人;4-基层部门专员
     */
    @TableField(value = "role_code", condition = LIKE)
    private String roleCode;
    /**
     * 展示状态
     */
    @TableField(value = "display_status", condition = LIKE)
    private String displayStatus;


}
