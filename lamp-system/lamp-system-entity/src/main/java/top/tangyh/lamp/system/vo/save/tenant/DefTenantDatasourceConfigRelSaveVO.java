package top.tangyh.lamp.system.vo.save.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 实体类
 * 租户的数据源
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
@Schema(title = "DefTenantDatasourceConfigRelSaveVO", description = "租户的数据源")
public class DefTenantDatasourceConfigRelSaveVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 租户id
     */
    @Schema(description = "租户id")
    @NotNull(message = "请填写租户id")
    private Long tenantId;
    /**
     * 数据源id
     */
    @Schema(description = "数据源id")
    @NotNull(message = "请填写数据源id")
    private Long datasourceConfigId;
    /**
     * 服务
     */
    @Schema(description = "服务")
    @NotEmpty(message = "请填写服务")
    @Size(max = 100, message = "服务长度不能超过100")
    private String application;

}
