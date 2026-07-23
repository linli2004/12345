package top.tangyh.lamp.base.vo.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.SuperEntity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 表单修改方法VO
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
@EqualsAndHashCode
@Builder
@Schema(description = "督办工单详情")
public class ChiefWorkOrderItemUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @NotNull(message = "请填写ID", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 批次编号
     */
    @Schema(description = "批次编号")
    @Size(max = 255, message = "批次编号长度不能超过{max}")
    private String batchNo;
    /**
     * 标题
     */
    @Schema(description = "标题")
    @Size(max = 255, message = "标题长度不能超过{max}")
    private String title;
    /**
     * 转办单工单编号
     */
    @Schema(description = "转办单工单编号")
    @Size(max = 255, message = "转办单工单编号长度不能超过{max}")
    private String workOrderNo;
    /**
     * 联系号码
     */
    @Schema(description = "联系号码")
    @Size(max = 255, message = "联系号码长度不能超过{max}")
    private String contactPhone;
    /**
     * 诉求号码
     */
    @Schema(description = "诉求号码")
    @Size(max = 255, message = "诉求号码长度不能超过{max}")
    private String appealPhone;
    /**
     * 诉求内容
     */
    @Schema(description = "诉求内容")
    @Size(max = 65535, message = "诉求内容长度不能超过{max}")
    private String appealContent;
    /**
     * 转办单回复内容
     */
    @Schema(description = "转办单回复内容")
    @Size(max = 65535, message = "转办单回复内容长度不能超过{max}")
    private String replyContent;
    /**
     * 诉求类型
     */
    @Schema(description = "诉求类型")
    @Size(max = 255, message = "诉求类型长度不能超过{max}")
    private String appealType;
    /**
     * 转办单满意度
     */
    @Schema(description = "转办单满意度")
    @Size(max = 255, message = "转办单满意度长度不能超过{max}")
    private String satisfaction;
    /**
     * 转办单不满意回访内容
     */
    @Schema(description = "转办单不满意回访内容")
    @Size(max = 65535, message = "转办单不满意回访内容长度不能超过{max}")
    private String unsatisfiedReason;
    /**
     * 线上督办单编号
     */
    @Schema(description = "线上督办单编号")
    @Size(max = 255, message = "线上督办单编号长度不能超过{max}")
    private String onlineSupervisionNo;
    /**
     * 督办单回访时间
     */
    @Schema(description = "督办单回访时间")
    private LocalDateTime supervisionReturnTime;
    /**
     * 督办单办结时间
     */
    @Schema(description = "督办单办结时间")
    private LocalDateTime supervisionFinishTime;
    /**
     * 督办单回复内容
     */
    @Schema(description = "督办单回复内容")
    @Size(max = 65535, message = "督办单回复内容长度不能超过{max}")
    private String supervisionReplyContent;
    /**
     * 督办单短信评价满意度
     */
    @Schema(description = "督办单短信评价满意度")
    @Size(max = 255, message = "督办单短信评价满意度长度不能超过{max}")
    private String supervisionSmsSatisfaction;
    /**
     * 督办单回访内容
     */
    @Schema(description = "督办单回访内容")
    @Size(max = 65535, message = "督办单回访内容长度不能超过{max}")
    private String supervisionReturnContent;
    /**
     * 督办单研判（一级）
     */
    @Schema(description = "督办单研判（一级）")
    @Size(max = 255, message = "督办单研判（一级）长度不能超过{max}")
    private String judgmentLevel1;
    /**
     * 督办单研判（二级）
     */
    @Schema(description = "督办单研判（二级）")
    @Size(max = 255, message = "督办单研判（二级）长度不能超过{max}")
    private String judgmentLevel2;
    /**
     * 最新办理结果(过程)
     */
    @Schema(description = "最新办理结果(过程)")
    @Size(max = 65535, message = "最新办理结果(过程)长度不能超过{max}")
    private String processResult;
    /**
     * 是否为最终办理结果
     */
    @Schema(description = "是否为最终办理结果")
    @Size(max = 255, message = "是否为最终办理结果长度不能超过{max}")
    private String isFinal;
    /**
     * 计划完成时间
     */
    @Schema(description = "计划完成时间")
    private LocalDateTime planFinishTime;
    /**
     * 分类调整
     */
    @Schema(description = "分类调整")
    @Size(max = 255, message = "分类调整长度不能超过{max}")
    private String categoryAdjust;
    /**
     * 承办单位
     */
    @Schema(description = "承办单位")
    @Size(max = 255, message = "承办单位长度不能超过{max}")
    private String undertakerUnit;
    /**
     * 最新办理结果(最终)
     */
    @Schema(description = "最新办理结果(最终)")
    @Size(max = 65535, message = "最新办理结果(最终)长度不能超过{max}")
    private String finalResult;

    /**
     * 工单分类id
     */
    @Schema(description = "工单分类id")
    private Long orderCategoryId;
    /**
     * 工单分类名称
     */
    @Schema(description = "工单分类名称")
    @Size(max = 255, message = "工单分类名称长度不能超过{max}")
    private String orderCategoryName;
    /**
     * 是否允许退回:0-不允许;1-允许;2-限6小时允许
     */
    @Schema(description = "是否允许退回:0-不允许;1-允许;2-限6小时允许")
    @Size(max = 255, message = "是否允许退回:0-不允许;1-允许;2-限6小时允许长度不能超过{max}")
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
    @Size(max = 255, message = "是否允许批示:1-允许;2-不允许长度不能超过{max}")
    private String allowComment;

    @Schema(description = "批示领导ID")
    private Long commentLeaderId;

    /**
     * 结案条件:0-单主办;1-任意结案;2-全部结案
     */
    @Schema(description = "结案条件:0-单主办;1-任意结案;2-全部结案")
    @Size(max = 255, message = "结案条件:0-单主办;1-任意结案;2-全部结案长度不能超过{max}")
    private String settleCondition;

}
