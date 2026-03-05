package top.tangyh.lamp.base.vo.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
 * 工单子表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:45:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "工单子表")
public class NormalWorkOrderTaskUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "任务ID")
    @NotNull(message = "请填写任务ID", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 关联主工单编号
     */
    @Schema(description = "关联主工单编号")
    @NotEmpty(message = "请填写关联主工单编号")
    @Size(max = 50, message = "关联主工单编号长度不能超过{max}")
    private String orderNo;
    /**
     * 业务节点编码
     */
    @Schema(description = "业务节点编码")
    @NotEmpty(message = "请填写业务节点编码")
    @Size(max = 10, message = "业务节点编码长度不能超过{max}")
    private String currentNodeCode;
    /**
     * 主办单位ID
     */
    @Schema(description = "主办单位ID")
    @NotNull(message = "请填写主办单位ID")
    private Long leadUnitId;
    /**
     * 协办单位ID集合(逗号分隔)
     */
    @Schema(description = "协办单位ID集合(逗号分隔)")
    @Size(max = 500, message = "协办单位ID集合(逗号分隔)长度不能超过{max}")
    private String coUnitIds;
    /**
     * 抄送单位ID集合(逗号分隔)
     */
    @Schema(description = "抄送单位ID集合(逗号分隔)")
    @Size(max = 500, message = "抄送单位ID集合(逗号分隔)长度不能超过{max}")
    private String ccUnitIds;
    /**
     * 处理截止时间
     */
    @Schema(description = "处理截止时间")
    private LocalDateTime processDeadline;
    /**
     * 优先级
     */
    @Schema(description = "优先级")
    @Size(max = 10, message = "优先级长度不能超过{max}")
    private String level;
    /**
     * 是否有效:1-有效;0-无效
     */
    @Schema(description = "是否有效:1-有效;0-无效")
    private String valid;
    /**
     * 创建人组织
     */
    @Schema(description = "创建人组织")
    private Long createdOrgId;


}
