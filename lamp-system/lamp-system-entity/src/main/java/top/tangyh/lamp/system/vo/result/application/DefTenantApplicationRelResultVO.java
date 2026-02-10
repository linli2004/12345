package top.tangyh.lamp.system.vo.result.application;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.tangyh.basic.annotation.echo.Echo;
import top.tangyh.basic.base.entity.Entity;
import top.tangyh.basic.interfaces.echo.EchoVO;
import top.tangyh.lamp.model.constant.EchoApi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实体类
 * 租户的应用
 * </p>
 *
 * @author zuihou
 * @since 2021-09-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(title = "DefTenantApplicationRelResultVO", description = "租户的应用")
public class DefTenantApplicationRelResultVO extends Entity<Long> implements Serializable, EchoVO {

    private static final long serialVersionUID = 1L;
    @Builder.Default
    private Map<String, Object> echoMap = new HashMap<>();

    @Schema(description = "主键")
    private Long id;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")

    @Echo(api = EchoApi.DEF_TENANT_SERVICE_IMPL_CLASS)
    private Long tenantId;
    /**
     * 应用ID
     */
    @Schema(description = "应用ID")

    @Echo(api = EchoApi.DEF_APPLICATION_SERVICE_IMPL_CLASS)
    private Long applicationId;
    /**
     * 过期时间
     */
    @Schema(description = "过期时间")

    private LocalDateTime expirationTime;

    @Schema(description = "是否过期")
    private Boolean expired;

    @Schema(description = "应用下的资源")
    private Collection<DefResourceResultVO> resourceList;
    @Schema(description = "选中的资源id")
    private List<Long> checkedList;
}
