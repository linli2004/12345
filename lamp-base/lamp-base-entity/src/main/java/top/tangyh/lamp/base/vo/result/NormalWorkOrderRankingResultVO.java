package top.tangyh.lamp.base.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 表单查询方法返回值VO
 * 普通工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:47:40
 */
@Data
@Schema(description = "统计排名")
public class NormalWorkOrderRankingResultVO {
    @Schema(description = "部门名称")
    private String name;        // 部门名称

    @Schema(description = "签收数量")
    private Long signCount;     // 签收数量

    @Schema(description = "办结数量")
    private Long finishCount;   // 办结数量

    @Schema(description = "签收排名")
    private Integer signRanking;   // 签收排名

    @Schema(description = "办结排名")
    private Integer finishRanking; // 办结排名
}
