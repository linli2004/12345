package top.tangyh.lamp.base.vo.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
public class NormalWorkOrderTaskActionVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "任务ID")
    private Long id;

    /**
     * 关联主工单编号
     */
    @Schema(description = "关联主工单编号")
    private String orderNo;

    @Schema(description = "业务节点编码")
    private String nodeCode;

    /**
     * 办理人ID
     */
    @Schema(description = "办理人ID")
    private Long operatorId;
    /**
     * 办理人姓名
     */
    @Schema(description = "办理人姓名")
    private String operatorName;
    /**
     * 联系方式
     */
    @Schema(description = "联系方式")
    private String operatorPhone;
    /**
     * 办理单位ID
     */
    @Schema(description = "办理单位ID")
    private Long deptId;
    /**
     * 办理单位名称
     */
    @Schema(description = "办理单位名称")
    private String deptName;
    /**
     * 办件方式
     */
    @Schema(description = "办件方式")
    private String processType;
    /**
     * 业务数据JSON(存储表单内容、附件地址、审批意见等)
     */
    @Schema(description = "业务数据JSON(存储表单内容、附件地址、审批意见等)")
    private String contentJson;

    //导入工单-----------------------------------------------------------------
    /**
     * 导入工单excel
     */
    @Schema(description = "导入excel")
    private MultipartFile file;

    //签收工单-----------------------------------------------------------------
    /**
     * 批量签收/交办/退回
     */
    @Schema(description = "关联主工单编号列表")
    private List<String> orderNoList;

    //交办工单-----------------------------------------------------------------
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
     * 街镇交办办结期限
     */
    private LocalDateTime processDeadline;
    /**
     * 主办单位ID集合(逗号分隔)
     */
    @Schema(description = "主办单位ID集合(逗号分隔)")
    private String leadUnitIds;
    /**
     * 协办单位ID集合(逗号分隔)
     */
    @Schema(description = "协办单位ID集合(逗号分隔)")
    private String coUnitIds;
    /**
     * 抄送单位ID集合(逗号分隔)
     */
    @Schema(description = "抄送单位ID集合(逗号分隔)")
    private String ccUnitIds;
    /**
     * 结案条件:0-单主办;1-任意结案;2-全部结案
     */
    @Schema(description = "结案条件:0-单主办;1-任意结案;2-全部结案")
    private String settleCondition;
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

    @Schema(description = "批示领导id")
    private Long commentLeaderId;

    //负责人，领导 审批-----------------------------------------------------------------
    /**
     * 审批结果
     */
    @Schema(description = "审批结果")
    Boolean auditResult;
    /**
     * 审批事项:1-结案;2-退回
     */
    @Schema(description = "审批事项:1-结案;2-退回")
    String auditType;

    @Schema(description = "分管领导id")
    private Long leadEmployeeId;
}
