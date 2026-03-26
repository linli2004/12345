package top.tangyh.lamp.base.vo.save;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 表单保存方法VO
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
@EqualsAndHashCode
@Builder
@Schema(description = "普通工单")
public class NormalWorkOrderSaveVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 工单编号
     */
    @Schema(description = "工单编号")
    @NotEmpty(message = "请填写工单编号")
    @Size(max = 64, message = "工单编号长度不能超过{max}")
    private String orderNo;
    /**
     * 工单标题
     */
    @Schema(description = "工单标题")
    @NotEmpty(message = "请填写工单标题")
    @Size(max = 500, message = "工单标题长度不能超过{max}")
    private String orderTitle;
    /**
     * 诉求内容
     */
    @Schema(description = "诉求内容")
    @Size(max = 65535, message = "诉求内容长度不能超过{max}")
    private String orderContent;
    /**
     * 诉求类型
     */
    @Schema(description = "诉求类型")
    @Size(max = 64, message = "诉求类型长度不能超过{max}")
    private String appealType;
    /**
     * 工单分类id
     */
    @Schema(description = "工单分类id")
    private Long orderCategoryId;
    /**
     * 工单分类名称
     */
    @Schema(description = "工单分类名称")
    @Size(max = 500, message = "工单分类名称长度不能超过{max}")
    private String orderCategoryName;
    /**
     * 区交办时间
     */
    @Schema(description = "区交办时间")
    private LocalDateTime regionAssignTime;
    /**
     * 区交办办结期限
     */
    @Schema(description = "区交办办结期限")
    private LocalDateTime regionDeadline;
    /**
     * 紧急程度
     */
    @Schema(description = "紧急程度")
    @Size(max = 32, message = "紧急程度长度不能超过{max}")
    private String urgency;
    /**
     * 诉求归口
     */
    @Schema(description = "诉求归口")
    @Size(max = 255, message = "诉求归口长度不能超过{max}")
    private String sourceDeptName;
    /**
     * 诉求渠道
     */
    @Schema(description = "诉求渠道")
    @Size(max = 64, message = "诉求渠道长度不能超过{max}")
    private String channel;
    /**
     * 求助人员
     */
    @Schema(description = "求助人员")
    @Size(max = 255, message = "求助人员长度不能超过{max}")
    private String helperName;
    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    @Size(max = 50, message = "联系电话长度不能超过{max}")
    private String contactPhone;
    /**
     * 性别
     */
    @Schema(description = "性别")
    @Size(max = 10, message = "性别长度不能超过{max}")
    private String gender;
    /**
     * 事件发生地
     */
    @Schema(description = "事件发生地")
    @Size(max = 255, message = "事件发生地长度不能超过{max}")
    private String incidentLocation;
    /**
     * 诉求详细地址
     */
    @Schema(description = "诉求详细地址")
    @Size(max = 512, message = "诉求详细地址长度不能超过{max}")
    private String address;
    /**
     * 是否疑难;[0-否 1-是]
     */
    @Schema(description = "是否疑难")
    private Boolean isDifficult;
    /**
     * 疑难内容
     */
    @Schema(description = "疑难内容")
    @Size(max = 65535, message = "疑难内容长度不能超过{max}")
    private String difficultContent;
    /**
     * 区交办主办单位
     */
    @Schema(description = "区交办主办单位")
    @Size(max = 255, message = "区交办主办单位长度不能超过{max}")
    private String hostDeptName;
    /**
     * 区交办协办单位
     */
    @Schema(description = "区交办协办单位")
    @Size(max = 255, message = "区交办协办单位长度不能超过{max}")
    private String assistDeptName;
    /**
     * 区交办抄送单位
     */
    @Schema(description = "区交办抄送单位")
    @Size(max = 255, message = "区交办抄送单位长度不能超过{max}")
    private String ccDeptName;
    /**
     * 结案条件:0-单主办;1-任意结案;2-全部结案
     */
    @Schema(description = "结案条件:0-单主办")
    @Size(max = 10, message = "结案条件:0-单主办长度不能超过{max}")
    private String settleCondition;
    /**
     * 最后操作时间
     */
    @Schema(description = "最后操作时间")
    private LocalDateTime lastOperateTime;
    /**
     * 创建人组织
     */
    @Schema(description = "创建人组织")
    private Long createdOrgId;
    /**
     * 是否允许退回:0-不允许;1-允许;2-限6小时允许
     */
    @Schema(description = "是否允许退回:0-不允许;1-允许;2-限6小时允许")
    private String allowBack;
    /**
     * 允许退回时间
     */
    @Schema(description = "允许退回时间")
    private LocalDateTime allowBackTime;
    /**
     * 是否允许批示:1-允许;2-不允许
     */
    @Schema(description = "是否允许批示:1-允许;2-不允许")
    private String allowComment;

    @Schema(description = "批示领导ID")
    private Long commentLeaderId;

}
