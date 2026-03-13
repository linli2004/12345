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
@EqualsAndHashCode(callSuper = true)
@Builder
@Schema(description = "工单办理暂存")
public class WorkOrderTemporaryResultVO extends Entity<Long> implements Serializable, EchoVO {
    @Serial
    private static final long serialVersionUID = 1L;
    @Builder.Default
    private final Map<String, Object> echoMap = MapUtil.newHashMap();

    @Schema(description = "暂存ID")
    private Long id;

    /**
     * 关联主工单编号
     */
    @Schema(description = "关联主工单编号")
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
    private String nodeName;
    /**
     * 办理人ID
     */
    @Schema(description = "办理人ID")
    private Long operatorId;
    /**
     * 业务数据JSON(存储表单内容、附件地址、审批意见等)
     */
    @Schema(description = "业务数据JSON(存储表单内容、附件地址、审批意见等)")
    private String contentJson;
    /**
     * 创建人组织
     */
    @Schema(description = "创建人组织")
    private Long createdOrgId;


}
