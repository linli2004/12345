package top.tangyh.lamp.system.vo.save.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@Schema(title = "DefTenantApplicationRelSaveVO", description = "租户的应用")
public class DefTenantApplicationRelSaveVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    @Size(min = 1, message = "请选择租户")
    @NotNull(message = "请选择租户")
    private List<Long> tenantIdList;
    /**
     * 应用ID
     */
    @Schema(description = "应用-资源id")
    @Size(min = 1, message = "请填写应用-资源id")
    @NotNull(message = "请选择应用-资源")
    private Map<Long, List<Long>> applicationResourceMap;
    /**
     * 过期时间
     */
    @Schema(description = "过期时间")
    private LocalDateTime expirationTime;

}
