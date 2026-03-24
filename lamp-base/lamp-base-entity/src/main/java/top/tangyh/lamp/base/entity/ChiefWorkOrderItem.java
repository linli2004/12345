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
 * 督办工单详情
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
@TableName("chief_work_order_item")
public class ChiefWorkOrderItem extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 批次编号
     */
    @TableField(value = "batch_no", condition = LIKE)
    private String batchNo;
    /**
     * 标题
     */
    @TableField(value = "title", condition = LIKE)
    private String title;
    /**
     * 转办单工单编号
     */
    @TableField(value = "work_order_no", condition = LIKE)
    private String workOrderNo;
    /**
     * 联系号码
     */
    @TableField(value = "contact_phone", condition = LIKE)
    private String contactPhone;
    /**
     * 诉求号码
     */
    @TableField(value = "appeal_phone", condition = LIKE)
    private String appealPhone;
    /**
     * 诉求内容
     */
    @TableField(value = "appeal_content", condition = LIKE)
    private String appealContent;
    /**
     * 转办单回复内容
     */
    @TableField(value = "reply_content", condition = LIKE)
    private String replyContent;
    /**
     * 诉求类型
     */
    @TableField(value = "appeal_type", condition = LIKE)
    private String appealType;
    /**
     * 转办单满意度
     */
    @TableField(value = "satisfaction", condition = LIKE)
    private String satisfaction;
    /**
     * 转办单不满意回访内容
     */
    @TableField(value = "unsatisfied_reason", condition = LIKE)
    private String unsatisfiedReason;
    /**
     * 线上督办单编号
     */
    @TableField(value = "online_supervision_no", condition = LIKE)
    private String onlineSupervisionNo;
    /**
     * 督办单回访时间
     */
    @TableField(value = "supervision_return_time", condition = EQUAL)
    private LocalDateTime supervisionReturnTime;
    /**
     * 督办单办结时间
     */
    @TableField(value = "supervision_finish_time", condition = EQUAL)
    private LocalDateTime supervisionFinishTime;
    /**
     * 督办单回复内容
     */
    @TableField(value = "supervision_reply_content", condition = LIKE)
    private String supervisionReplyContent;
    /**
     * 督办单短信评价满意度
     */
    @TableField(value = "supervision_sms_satisfaction", condition = LIKE)
    private String supervisionSmsSatisfaction;
    /**
     * 督办单回访内容
     */
    @TableField(value = "supervision_return_content", condition = LIKE)
    private String supervisionReturnContent;
    /**
     * 督办单研判（一级）
     */
    @TableField(value = "judgment_level1", condition = LIKE)
    private String judgmentLevel1;
    /**
     * 督办单研判（二级）
     */
    @TableField(value = "judgment_level2", condition = LIKE)
    private String judgmentLevel2;
    /**
     * 最新办理结果(过程)
     */
    @TableField(value = "process_result", condition = LIKE)
    private String processResult;
    /**
     * 是否为最终办理结果
     */
    @TableField(value = "is_final", condition = LIKE)
    private String isFinal;
    /**
     * 计划完成时间
     */
    @TableField(value = "plan_finish_time", condition = EQUAL)
    private LocalDateTime planFinishTime;
    /**
     * 分类调整
     */
    @TableField(value = "category_adjust", condition = LIKE)
    private String categoryAdjust;
    /**
     * 承办单位
     */
    @TableField(value = "undertaker_unit", condition = LIKE)
    private String undertakerUnit;
    /**
     * 最新办理结果(最终)
     */
    @TableField(value = "final_result", condition = LIKE)
    private String finalResult;

    /**
     * 工单分类id
     */
    @TableField(value = "order_category_id", condition = EQUAL)
    private Long orderCategoryId;
    /**
     * 工单分类名称
     */
    @TableField(value = "order_category_name", condition = LIKE)
    private String orderCategoryName;
    /**
     * 是否允许退回:0-不允许;1-允许;2-限6小时允许
     */
    @TableField(value = "allow_back", condition = EQUAL)
    private String allowBack;

    /**
     * 允许退回时间
     */
    @TableField(value = "allow_back_time", condition = EQUAL)
    private LocalDateTime allowBackTime;

    /**
     * 是否允许批示:1-允许;2-不允许
     */
    @TableField(value = "allow_comment", condition = EQUAL)
    private String allowComment;

    /**
     * 结案条件:0-单主办;1-任意结案;2-全部结案
     */
    @TableField(value = "settle_condition", condition = LIKE)
    private String settleCondition;

    @TableField(value = "last_operate_time", condition = EQUAL)
    private LocalDateTime lastOperateTime;
}
