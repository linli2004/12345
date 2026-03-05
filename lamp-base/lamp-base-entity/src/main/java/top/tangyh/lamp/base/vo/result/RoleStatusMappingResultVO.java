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
 * 角色视图映射配置表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:49:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@Schema(description = "角色视图映射配置表")
public class RoleStatusMappingResultVO extends Entity<Long> implements Serializable, EchoVO {
    @Serial
    private static final long serialVersionUID = 1L;
    @Builder.Default
    private final Map<String, Object> echoMap = MapUtil.newHashMap();

    @Schema(description = "ID")
    private Long id;

    /**
     * 业务节点编码
     */
    @Schema(description = "业务节点编码")
    private String nodeCode;
    /**
     * 角色编码:1-镇级网格专员;2-基层部门分管领导;3-基层部门负责人;4-基层部门专员
     */
    @Schema(description = "角色编码:1-镇级网格专员")
    private String roleCode;
    /**
     * 展示状态
     */
    @Schema(description = "展示状态")
    private String displayStatus;


}
