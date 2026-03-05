package top.tangyh.lamp.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.TreeEntity;

import java.io.Serial;

import static com.baomidou.mybatisplus.annotation.SqlCondition.EQUAL;
import static top.tangyh.lamp.model.constant.Condition.LIKE;


/**
 * <p>
 * 实体类
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
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("work_order_category")
public class WorkOrderCategory extends TreeEntity<WorkOrderCategory, Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @TableField(value = "name", condition = LIKE)
    private String name;
    /**
     * 树层级
     */
    @TableField(value = "tree_grade", condition = EQUAL)
    private Integer treeGrade;
    /**
     * 状态;[0-禁用 1-启用]
     */
    @TableField(value = "state", condition = EQUAL)
    private Boolean state;
    /**
     * 备注
     */
    @TableField(value = "remarks", condition = LIKE)
    private String remarks;
    /**
     * 创建人组织
     */
    @TableField(value = "created_org_id", condition = EQUAL)
    private Long createdOrgId;


}
