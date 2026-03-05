package top.tangyh.lamp.base.vo.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;


/**
 * <p>
 * 表单查询条件VO
 * 工单办理动态
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:48:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "工单办理动态")
public class WorkOrderDynamicPageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "动态ID")
    private Long id;

    /**
     * 关联主工单编号
     */
    @Schema(description = "关联主工单编号")
    private String orderNo;
    /**
     * 关联子任务ID
     */
    @Schema(description = "关联子任务ID")
    private Long taskId;
    /**
     * 业务节点
     */
    @Schema(description = "业务节点")
    private String nodeCode;
    /**
     * 节点名称描述
     */
    @Schema(description = "节点名称描述")
    private String nodeName;
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
    /**
     * 创建人组织
     */
    @Schema(description = "创建人组织")
    private Long createdOrgId;


}
