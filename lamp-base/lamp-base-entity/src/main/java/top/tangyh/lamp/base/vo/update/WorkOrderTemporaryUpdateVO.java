package top.tangyh.lamp.base.vo.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.SuperEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 表单修改方法VO
 * 工单办理暂存
 * </p>
 *
 * @author lunar
 * @date 2026-03-12 11:50:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "工单办理暂存")
public class WorkOrderTemporaryUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "暂存ID")
    @NotNull(message = "请填写暂存ID", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 关联主工单编号
     */
    @Schema(description = "关联主工单编号")
    @Size(max = 50, message = "关联主工单编号长度不能超过{max}")
    private String orderNo;
    /**
     * 关联子任务ID
     */
    @Schema(description = "关联子任务ID")
    private String taskIds;
    /**
     * 节点及操作名称
     */
    @Schema(description = "节点及操作名称")
    @Size(max = 100, message = "节点及操作名称长度不能超过{max}")
    private String nodeName;
    /**
     * 办理人ID
     */
    @Schema(description = "办理人ID")
    @NotNull(message = "请填写办理人ID")
    private Long operatorId;
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
