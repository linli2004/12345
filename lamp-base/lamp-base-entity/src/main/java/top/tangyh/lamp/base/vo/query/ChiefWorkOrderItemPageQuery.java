package top.tangyh.lamp.base.vo.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * <p>
 * 表单查询条件VO
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
public class ChiefWorkOrderItemPageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    /**
     * 主办单位名称
     */
    @Schema(description = "主办单位名称")
    private String hostDeptName;
    /**
     * 协办单位名称
     */
    @Schema(description = "协办单位名称")
    private String assistDeptName;
    /**
     * 抄送单位名称
     */
    @Schema(description = "抄送单位名称")
    private String ccDeptName;

    /**
     * 是否联合查询
     */
    @Schema(description = "是否联合查询")
    private Boolean isJointQuery;
    /**
     * 关键字
     */
    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "展示状态")
    private String displayStatus;

    @Schema(description = "工单编号集合")
    private List<String> orderNoList;

    @Schema(description = "紧急程度")
    private String urgency;

    @Schema(description = "诉求来源")
    private String sourceDeptName;

    @Schema(description = "来源渠道")
    private String channel;

    @Schema(description = "求助人")
    private String helperName;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "事发位置")
    private String incidentLocation;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "是否疑难")
    private Boolean isDifficult;

    @Schema(description = "角色编码:1-镇级网格专员;2-基层部门分管领导;3-基层部门负责人;4-基层部门专员")
    private String roleCode;

    @Schema(description = "主办单位ID")
    private String leadUnitId;


    @Schema(description = "临近超期的小时（如填48,就是48小时内将要过期的，包含已过期的）")
    private String expireHour;

    @Schema(description = "协办或抄送单位查询 1-co(协办) 2-cc(抄送)")
    private String coOrccType;

    @Schema(description = "co或cc的id")
    private Long coOrccId;


    private Long operatorId;

    private List<Long> leadUnitIdList;
    private List<String> prefixList;
    private String regionDeadlineStart;
    private String regionDeadlineEnd;

    @Schema(description = "1:一日内到期")
    private String orderFlag;

    private String lastOperateTimeStart;
    private String lastOperateTimeEnd;

    @Schema(description = "分管领导id")
    private Long leadEmployeeId;
}
