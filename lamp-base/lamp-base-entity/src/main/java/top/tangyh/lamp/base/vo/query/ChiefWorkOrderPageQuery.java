package top.tangyh.lamp.base.vo.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 表单查询条件VO
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
public class ChiefWorkOrderPageQuery implements Serializable {

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
     * 名称
     */
    @Schema(description = "名称")
    private String name;
    /**
     * 状态
     */
    @Schema(description = "状态")
    private String status;
    /**
     * 导入时间
     */
    @Schema(description = "导入时间")
    private LocalDateTime importTime;


}
