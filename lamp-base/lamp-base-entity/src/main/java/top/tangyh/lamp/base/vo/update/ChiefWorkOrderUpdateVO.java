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
 * 督办工单
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
@Schema(description = "督办工单")
public class ChiefWorkOrderUpdateVO implements Serializable {
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
     * 名称
     */
    @Schema(description = "名称")
    @NotEmpty(message = "请填写名称")
    @Size(max = 255, message = "名称长度不能超过{max}")
    private String name;
    /**
     * 状态
     */
    @Schema(description = "状态")
    @Size(max = 255, message = "状态长度不能超过{max}")
    private String status;
    /**
     * 导入时间
     */
    @Schema(description = "导入时间")
    private LocalDateTime importTime;


}
