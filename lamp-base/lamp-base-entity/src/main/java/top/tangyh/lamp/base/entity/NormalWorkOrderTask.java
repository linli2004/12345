package top.tangyh.lamp.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.Entity;

import java.io.Serial;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.EQUAL;
import static top.tangyh.lamp.model.constant.Condition.LIKE;


/**
 * <p>
 * 实体类
 * 工单子表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:45:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("normal_work_order_task")
public class NormalWorkOrderTask extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联主工单编号
     */
    @TableField(value = "order_no", condition = LIKE)
    private String orderNo;
    /**
     * 业务节点编码
     */
    @TableField(value = "current_node_code", condition = LIKE)
    private String currentNodeCode;
    /**
     * 主办单位ID
     */
    @TableField(value = "lead_unit_id", condition = EQUAL)
    private Long leadUnitId;
    /**
     * 协办单位ID集合(逗号分隔)
     */
    @TableField(value = "co_unit_ids", condition = LIKE)
    private String coUnitIds;
    /**
     * 抄送单位ID集合(逗号分隔)
     */
    @TableField(value = "cc_unit_ids", condition = LIKE)
    private String ccUnitIds;
    /**
     * 处理截止时间
     */
    @TableField(value = "process_deadline", condition = EQUAL)
    private LocalDateTime processDeadline;
    /**
     * 优先级
     */
    @TableField(value = "level", condition = LIKE)
    private String level;

    /**
     * 是否有效:1-有效;0-无效
     */
    @TableField(value = "valid", condition = EQUAL)
    private String valid;
    /**
     * 创建人组织
     */
    @TableField(value = "created_org_id", condition = EQUAL)
    private Long createdOrgId;


}
