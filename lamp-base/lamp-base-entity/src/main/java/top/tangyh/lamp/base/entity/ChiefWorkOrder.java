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
 * 督办工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("chief_work_order")
public class ChiefWorkOrder extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 批次编号
     */
    @TableField(value = "batch_no", condition = LIKE)
    private String batchNo;
    /**
     * 名称
     */
    @TableField(value = "name", condition = LIKE)
    private String name;
    /**
     * 状态
     */
    @TableField(value = "status", condition = LIKE)
    private String status;
    /**
     * 导入时间
     */
    @TableField(value = "import_time", condition = EQUAL)
    private LocalDateTime importTime;


}
