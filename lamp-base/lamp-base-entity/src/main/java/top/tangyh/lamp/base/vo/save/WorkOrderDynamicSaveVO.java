package top.tangyh.lamp.base.vo.save;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 表单保存方法VO
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
public class WorkOrderDynamicSaveVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联主工单编号
     */
    @Schema(description = "关联主工单编号")
    @NotEmpty(message = "请填写关联主工单编号")
    @Size(max = 50, message = "关联主工单编号长度不能超过{max}")
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
    @NotEmpty(message = "请填写业务节点")
    @Size(max = 10, message = "业务节点长度不能超过{max}")
    private String nodeCode;
    /**
     * 节点名称描述
     */
    @Schema(description = "节点名称描述")
    @Size(max = 100, message = "节点名称描述长度不能超过{max}")
    private String nodeName;
    /**
     * 办理人ID
     */
    @Schema(description = "办理人ID")
    @NotNull(message = "请填写办理人ID")
    private Long operatorId;
    /**
     * 办理人姓名
     */
    @Schema(description = "办理人姓名")
    @NotEmpty(message = "请填写办理人姓名")
    @Size(max = 50, message = "办理人姓名长度不能超过{max}")
    private String operatorName;
    /**
     * 联系方式
     */
    @Schema(description = "联系方式")
    @Size(max = 20, message = "联系方式长度不能超过{max}")
    private String operatorPhone;
    /**
     * 办理单位ID
     */
    @Schema(description = "办理单位ID")
    @NotNull(message = "请填写办理单位ID")
    private Long deptId;
    /**
     * 办理单位名称
     */
    @Schema(description = "办理单位名称")
    @NotEmpty(message = "请填写办理单位名称")
    @Size(max = 100, message = "办理单位名称长度不能超过{max}")
    private String deptName;
    /**
     * 办件方式
     */
    @Schema(description = "办件方式")
    @Size(max = 50, message = "办件方式长度不能超过{max}")
    private String processType;
    /**
     * 业务数据JSON(存储表单内容、附件地址、审批意见等)
     */
    @Schema(description = "业务数据JSON(存储表单内容、附件地址、审批意见等)")
    @Size(max = 1073741824, message = "业务数据JSON(存储表单内容、附件地址、审批意见等)长度不能超过{max}")
    private String contentJson;
    /**
     * 创建人组织
     */
    @Schema(description = "创建人组织")
    private Long createdOrgId;


}
