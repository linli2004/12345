package top.tangyh.lamp.base.vo.result;

import cn.hutool.core.map.MapUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;
import top.tangyh.basic.annotation.echo.Echo;
import top.tangyh.basic.base.entity.Entity;
import top.tangyh.basic.interfaces.echo.EchoVO;
import top.tangyh.lamp.model.constant.EchoApi;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Echo(api = EchoApi.ORG_ID_CLASS)
    private Long leadUnitId;
    /**
     * 协办单位ID集合(逗号分隔)
     */
    @Schema(description = "协办单位ID集合(逗号分隔)")
    private String coUnitIds;

    @Echo(api = EchoApi.ORG_ID_CLASS)
    @Schema(description = "协办单位ID集合")
    private List<Long> coUnitIdList;

    public void setCoUnitIds(String coUnitIds) {
        this.coUnitIds = coUnitIds;
        this.coUnitIdList = splitToLongList(coUnitIds);
    }

    public void setCcUnitIds(String ccUnitIds) {
        this.ccUnitIds = ccUnitIds;
        this.ccUnitIdList = splitToLongList(ccUnitIds);
    }

    /**
     * 抄送单位ID集合(逗号分隔)
     */
    @Schema(description = "抄送单位ID集合(逗号分隔)")
    private String ccUnitIds;

    @Echo(api = EchoApi.ORG_ID_CLASS)
    @Schema(description = "抄送单位ID集合")
    private List<Long> ccUnitIdList;
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

    @Schema(description = "当前状态")
    private String currentNodeName;

    @Schema(description = "基层领导")
    private String deptLeader;

    @Schema(description = "基层负责人")
    private String deptDirector;
    private String readRandomStr;

    public static List<Long> splitToLongList(String str) {
        if (!StringUtils.hasText(str)) {
            return Collections.emptyList();
        }
        try {
            return Arrays.stream(str.split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }
    }
}
