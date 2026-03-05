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
 * 普通工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:47:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("normal_work_order")
public class NormalWorkOrder extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 工单编号
     */
    @TableField(value = "order_no", condition = LIKE)
    private String orderNo;
    /**
     * 工单标题
     */
    @TableField(value = "order_title", condition = LIKE)
    private String orderTitle;
    /**
     * 诉求内容
     */
    @TableField(value = "order_content", condition = LIKE)
    private String orderContent;
    /**
     * 诉求类型
     */
    @TableField(value = "appeal_type", condition = LIKE)
    private String appealType;
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
     * 区交办时间
     */
    @TableField(value = "region_assign_time", condition = EQUAL)
    private LocalDateTime regionAssignTime;
    /**
     * 区交办办结期限
     */
    @TableField(value = "region_deadline", condition = EQUAL)
    private LocalDateTime regionDeadline;
    /**
     * 紧急程度
     */
    @TableField(value = "urgency", condition = LIKE)
    private String urgency;
    /**
     * 诉求归口
     */
    @TableField(value = "source_dept_name", condition = LIKE)
    private String sourceDeptName;
    /**
     * 诉求渠道
     */
    @TableField(value = "channel", condition = LIKE)
    private String channel;
    /**
     * 求助人员
     */
    @TableField(value = "helper_name", condition = LIKE)
    private String helperName;
    /**
     * 联系电话
     */
    @TableField(value = "contact_phone", condition = LIKE)
    private String contactPhone;
    /**
     * 性别
     */
    @TableField(value = "gender", condition = LIKE)
    private String gender;
    /**
     * 事件发生地
     */
    @TableField(value = "incident_location", condition = LIKE)
    private String incidentLocation;
    /**
     * 诉求详细地址
     */
    @TableField(value = "address", condition = LIKE)
    private String address;
    /**
     * 是否疑难;[0-否 1-是]
     */
    @TableField(value = "is_difficult", condition = EQUAL)
    private Boolean isDifficult;
    /**
     * 疑难内容
     */
    @TableField(value = "difficult_content", condition = LIKE)
    private String difficultContent;
    /**
     * 区交办主办单位
     */
    @TableField(value = "host_dept_name", condition = LIKE)
    private String hostDeptName;
    /**
     * 区交办协办单位
     */
    @TableField(value = "assist_dept_name", condition = LIKE)
    private String assistDeptName;
    /**
     * 区交办抄送单位
     */
    @TableField(value = "cc_dept_name", condition = LIKE)
    private String ccDeptName;
    /**
     * 结案条件:0-单主办;1-任意结案;2-全部结案
     */
    @TableField(value = "settle_condition", condition = LIKE)
    private String settleCondition;
    /**
     * 最后操作时间
     */
    @TableField(value = "last_operate_time", condition = EQUAL)
    private LocalDateTime lastOperateTime;
    /**
     * 创建人组织
     */
    @TableField(value = "created_org_id", condition = EQUAL)
    private Long createdOrgId;
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


}
