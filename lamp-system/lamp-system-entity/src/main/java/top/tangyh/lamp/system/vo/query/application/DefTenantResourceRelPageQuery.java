package top.tangyh.lamp.system.vo.query.application;

import io.swagger.v3.oas.annotations.media.Schema;
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
 * @since 2021-09-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(title = "DefTenantResourceRelPageQuery", description = "租户的资源")
public class DefTenantResourceRelPageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    @Schema(description = "资源ID")
    private Long resourceId;
    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Long tenantId;

}
