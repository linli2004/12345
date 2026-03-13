package top.tangyh.lamp.base.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.cglib.core.Local;
import top.tangyh.lamp.base.entity.WorkOrderDynamic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NormalWorkOrderExport {


    /**
     * 工单编号
     */
    @Schema(description = "工单编号")
    private String orderNo;
    /**
     * 工单标题
     */
    @Schema(description = "工单标题")
    private String orderTitle;
    /**
     * 诉求内容
     */
    @Schema(description = "诉求内容")
    private String orderContent;
    /**
     * 诉求类型
     */
    @Schema(description = "诉求类型")
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
    private String orderCategoryName;
    /**
     * 区交办时间
     */
    @Schema(description = "区交办时间")
    private LocalDateTime regionAssignTime;
    private String regionAssignTimeStr;
    /**
     * 区交办办结期限
     */
    @Schema(description = "区交办办结期限")
    private LocalDateTime regionDeadline;
    private String regionDeadlineStr;
    /**
     * 紧急程度
     */
    @Schema(description = "紧急程度")
    private String urgency;
    /**
     * 诉求归口
     */
    @Schema(description = "诉求归口")
    private String sourceDeptName;
    /**
     * 诉求渠道
     */
    @Schema(description = "诉求渠道")
    private String channel;
    /**
     * 求助人员
     */
    @Schema(description = "求助人员")
    private String helperName;
    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String contactPhone;
    /**
     * 性别
     */
    @Schema(description = "性别")
    private String gender;
    /**
     * 事件发生地
     */
    @Schema(description = "事件发生地")
    private String incidentLocation;
    /**
     * 诉求详细地址
     */
    @Schema(description = "诉求详细地址")
    private String address;
    /**
     * 是否疑难;[0-否 1-是]
     */
    @Schema(description = "是否疑难")
    private Boolean isDifficult;
    private String isDifficultStr;
    /**
     * 疑难内容
     */
    @Schema(description = "疑难内容")
    private String difficultContent;
    /**
     * 区交办主办单位
     */
    @Schema(description = "区交办主办单位")
    private String hostDeptName;
    /**
     * 区交办协办单位
     */
    @Schema(description = "区交办协办单位")
    private String assistDeptName;
    /**
     * 区交办抄送单位
     */
    @Schema(description = "区交办抄送单位")
    private String ccDeptName;
    /**
     * 结案条件:0-单主办;1-任意结案;2-全部结案
     */
    @Schema(description = "结案条件:0-单主办")
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

    private List<NormalWorkOrderTaskResultVO> workOrderTaskList;

    private WorkOrderDynamic finishOrBackDynamic;

    private String exportTime;

    private String assistStatus;
    private String municipalHostDeptName;
    private String municipalAssistDeptName;
    private LocalDateTime municipalAssignTime;
    private LocalDateTime municipalDeadline;

    private String deptName;
    private LocalDateTime finishTime;
    private String operatorName;
    private String isExpire;
    private String replier;
    private String isOnSite;
    private String replyTime;
    private String closingUnit;
    private String replyResult;
    private Integer isFinalReply;
    private String publicReplyType;
    private String internalReplyType;
    private Integer notifyCitizenFirst;
    private String publicReplyContent;
    private String citizenReplyContent;
    private String contactCitizenFirst;
    private String internalReplyContent;

    private String orderStatus;

    private String returnReason;
    private String returnType;
}
