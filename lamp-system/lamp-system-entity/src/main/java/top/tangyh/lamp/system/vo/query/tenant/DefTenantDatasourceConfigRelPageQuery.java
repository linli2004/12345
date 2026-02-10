package top.tangyh.lamp.system.vo.query.tenant;

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
 * 租户的数据源
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
@Schema(title = "DefTenantDatasourceConfigRelPageQuery", description = "租户的数据源")
public class DefTenantDatasourceConfigRelPageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租户id
     */
    @Schema(description = "租户id")
    private Long tenantId;
    /**
     * 数据源id
     */
    @Schema(description = "数据源id")
    private Long datasourceConfigId;
    /**
     * 服务
     */
    @Schema(description = "服务")
    private String application;

}
