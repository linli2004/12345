package top.tangyh.lamp.base.vo.result;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.Entity;
import top.tangyh.basic.interfaces.echo.EchoVO;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import static com.baomidou.mybatisplus.annotation.SqlCondition.EQUAL;
import static top.tangyh.lamp.model.constant.Condition.LIKE;


/**
 * <p>
 * 实体类
 * 督办工单子表
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
@EqualsAndHashCode(callSuper = true)
@Builder
public class ChiefWorkOrderTaskResultVO extends Entity<Long> implements Serializable, EchoVO {

    @Serial
    private static final long serialVersionUID = 1L;
    @Builder.Default
    private final Map<String, Object> echoMap = MapUtil.newHashMap();

    @Schema(description = "主键")
    private Long id;
    /**
     * 关联主工单编号
     */
    private String orderNo;
    /**
     * 业务节点编码
     */
    private String currentNodeCode;
    /**
     * 主办单位ID
     */
    private Long leadUnitId;
    /**
     * 协办单位ID集合(逗号分隔)
     */
    private String coUnitIds;
    /**
     * 抄送单位ID集合(逗号分隔)
     */
    private String ccUnitIds;
    /**
     * 处理截止时间
     */
    private LocalDateTime processDeadline;
    /**
     * 优先级
     */
    private String level;

    /**
     * 是否有效:1-有效;0-无效
     */
    private String valid;
    /**
     * 创建人组织
     */
    private Long createdOrgId;

    @Schema(description = "当前状态")
    private String currentNodeName;

    @Schema(description = "基层领导")
    private String deptLeader;

    @Schema(description = "基层负责人")
    private String deptDirector;
    private String readRandomStr;

}
