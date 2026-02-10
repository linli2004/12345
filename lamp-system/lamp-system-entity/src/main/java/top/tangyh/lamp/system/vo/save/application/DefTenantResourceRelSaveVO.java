package top.tangyh.lamp.system.vo.save.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 实体类
 * 租户的资源
 * </p>
 *
 * @author zuihou
 * @since 2021-09-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(title = "DefTenantResourceRelSaveVO", description = "租户的资源")
public class DefTenantResourceRelSaveVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    @NotNull(message = "请填写租户ID")
    private Long tenantId;
    /**
     * 应用Id
     */
    @Schema(description = "应用Id")
    @NotNull(message = "请填写应用Id")
    private Long applicationId;
    /**
     * 资源ID
     */
    @Schema(description = "资源ID")
    @NotNull(message = "请填写资源ID")
    private Long resourceId;

}
