package top.tangyh.lamp.base.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.tangyh.lamp.base.entity.ChiefWorkOrderDynamic;
import top.tangyh.lamp.base.entity.ChiefWorkOrderItem;
import top.tangyh.lamp.msg.entity.ExtendMsg;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChiefWorkOrderExport {

    @Schema(description = "ID")
    private Long id;

    /**
     * 批次编号
     */
    @Schema(description = "批次编号")
    private String batchNo;
    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;
    /**
     * 转办单工单编号
     */
    @Schema(description = "转办单工单编号")
    private String workOrderNo;
    /**
     * 联系号码
     */
    @Schema(description = "联系号码")
    private String contactPhone;
    /**
     * 诉求号码
     */
    @Schema(description = "诉求号码")
    private String appealPhone;
    /**
     * 诉求内容
     */
    @Schema(description = "诉求内容")
    private String appealContent;
    /**
     * 转办单回复内容
     */
    @Schema(description = "转办单回复内容")
    private String replyContent;
    /**
     * 诉求类型
     */
    @Schema(description = "诉求类型")
    private String appealType;
    /**
     * 转办单满意度
     */
    @Schema(description = "转办单满意度")
    private String satisfaction;
    /**
     * 转办单不满意回访内容
     */
    @Schema(description = "转办单不满意回访内容")
    private String unsatisfiedReason;
    /**
     * 线上督办单编号
     */
    @Schema(description = "线上督办单编号")
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
    private String supervisionReplyContent;
    /**
     * 督办单短信评价满意度
     */
    @Schema(description = "督办单短信评价满意度")
    private String supervisionSmsSatisfaction;
    /**
     * 督办单回访内容
     */
    @Schema(description = "督办单回访内容")
    private String supervisionReturnContent;
    /**
     * 督办单研判（一级）
     */
    @Schema(description = "督办单研判（一级）")
    private String judgmentLevel1;
    /**
     * 督办单研判（二级）
     */
    @Schema(description = "督办单研判（二级）")
    private String judgmentLevel2;
    /**
     * 最新办理结果(过程)
     */
    @Schema(description = "最新办理结果(过程)")
    private String processResult;
    /**
     * 是否为最终办理结果
     */
    @Schema(description = "是否为最终办理结果")
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
    private String categoryAdjust;
    /**
     * 承办单位
     */
    @Schema(description = "承办单位")
    private String undertakerUnit;
    /**
     * 最新办理结果(最终)
     */
    @Schema(description = "最新办理结果(最终)")
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
    private String orderCategoryName;
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

    /**
     * 结案条件:0-单主办;1-任意结案;2-全部结案
     */
    @Schema(description = "结案条件:0-单主办;1-任意结案;2-全部结案")
    private String settleCondition;
    private ChiefWorkOrderDynamic finishOrBackDynamic;
    private ChiefWorkOrderItem chiefWorkOrderItem;
    private String status;
    private LocalDateTime lastOperateTime;


    private List<ChiefWorkOrderTaskResultVO> workOrderTaskList;
    private List<ExtendMsg> urgeList;

    private String processDept = "津南区";
}
