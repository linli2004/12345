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
@Schema(description = "签收未分类工单")
public class SignCategoryIsNullNormalWorkOrderResultVO {

    /**
     * 工单分类名称
     */
    @Schema(description = "工单分类名称")
    private String categoryName;

    private Long total;
}
