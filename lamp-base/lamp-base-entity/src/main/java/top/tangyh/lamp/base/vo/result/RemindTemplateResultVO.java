package top.tangyh.lamp.base.vo.result;

import cn.hutool.core.map.MapUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.Entity;
import top.tangyh.basic.interfaces.echo.EchoVO;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 表单查询方法返回值VO
 * 提醒模板
 * </p>
 *
 * @author lunar
 * @date 2026-03-10 08:39:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@Schema(description = "提醒模板")
public class RemindTemplateResultVO extends Entity<Long> implements Serializable, EchoVO {
    @Serial
    private static final long serialVersionUID = 1L;
    @Builder.Default
    private final Map<String, Object> echoMap = MapUtil.newHashMap();

    @Schema(description = "ID")
    private Long id;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;
    /**
     * 展示内容
     */
    @Schema(description = "展示内容")
    private String displayContent;
    /**
     * 创建人组织
     */
    @Schema(description = "创建人组织")
    private Long createdOrgId;


}
