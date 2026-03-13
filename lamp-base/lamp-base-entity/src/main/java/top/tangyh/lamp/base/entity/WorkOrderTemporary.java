package top.tangyh.lamp.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.Entity;

import java.io.Serial;

import static com.baomidou.mybatisplus.annotation.SqlCondition.EQUAL;
import static top.tangyh.lamp.model.constant.Condition.LIKE;


/**
 * <p>
 * 实体类
 * 工单办理暂存
 * </p>
 *
 * @author lunar
 * @date 2026-03-12 11:50:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("work_order_temporary")
public class WorkOrderTemporary extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联主工单编号
     */
    @TableField(value = "order_no", condition = LIKE)
    private String orderNo;
    /**
     * 关联子任务ID
     */
    @TableField(value = "task_ids", condition = EQUAL)
    private String taskIds;
    /**
     * 节点及操作名称
     */
    @TableField(value = "node_name", condition = LIKE)
    private String nodeName;
    /**
     * 办理人ID
     */
    @TableField(value = "operator_id", condition = EQUAL)
    private Long operatorId;
    /**
     * 业务数据JSON(存储表单内容、附件地址、审批意见等)
     */
    @TableField(value = "content_json", condition = LIKE)
    private String contentJson;
    /**
     * 创建人组织
     */
    @TableField(value = "created_org_id", condition = EQUAL)
    private Long createdOrgId;


}
