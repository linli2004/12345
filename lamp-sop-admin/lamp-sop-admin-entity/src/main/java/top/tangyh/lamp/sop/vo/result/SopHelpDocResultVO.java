package top.tangyh.lamp.sop.vo.result;

import cn.hutool.core.map.MapUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.TreeEntity;
import top.tangyh.basic.interfaces.echo.EchoVO;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 表单查询方法返回值VO
 * 帮助内容
 * </p>
 *
 * @author zuihou
 * @date 2025-12-18 12:21:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@Schema(description = "帮助内容表")
public class SopHelpDocResultVO extends TreeEntity<SopHelpDocResultVO, Long> implements Serializable, EchoVO {
    @Serial
    private static final long serialVersionUID = 1L;
    @Builder.Default
    private final Map<String, Object> echoMap = MapUtil.newHashMap();

    @Schema(description = "id")
    private Long id;

    /**
     * 文档名称
     */
    @Schema(description = "文档名称")
    private String label;
    /**
     * 状态
     * 1：启用，0：禁用
     */
    @Schema(description = "状态")
    private Boolean status;
    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;
    /**
     * 内容类型
     * 1-Markdown,2-富文本
     */
    @Schema(description = "内容类型")
    private Integer contentType;
    /**
     * 父级id
     */
    @Schema(description = "父级id")
    private Long parentId;


}
