package top.tangyh.lamp.base.vo.result;

import cn.hutool.core.map.MapUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.Entity;
import top.tangyh.basic.interfaces.echo.EchoVO;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 表单查询方法返回值VO
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
@EqualsAndHashCode(callSuper = true)
@Builder
@Schema(description = "工单子表")
public class NormalWorkOrderTaskResultVO extends Entity<Long> implements Serializable, EchoVO {
    @Serial
    private static final long serialVersionUID = 1L;
    @Builder.Default
    private final Map<String, Object> echoMap = MapUtil.newHashMap();

    @Schema(description = "任务ID")
    private Long id;

    /**
     * 关联主工单编号
     */
    @Schema(description = "关联主工单编号")
    private String orderNo;
    /**
     * 业务节点编码
     */
    @Schema(description = "业务节点编码")
    private String currentNodeCode;
    /**
     * 主办单位ID
     */
    @Schema(description = "主办单位ID")
    private Long leadUnitId;
    /**
     * 协办单位ID集合(逗号分隔)
     */
    @Schema(description = "协办单位ID集合(逗号分隔)")
    private String coUnitIds;
    /**
     * 抄送单位ID集合(逗号分隔)
     */
    @Schema(description = "抄送单位ID集合(逗号分隔)")
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
