package top.tangyh.lamp.system.vo.save.tenant;

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
 * 员工
 * </p>
 *
 * @author zuihou
 * @since 2021-10-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(title = "DefUserTenantRelSaveVO", description = "员工")
public class DefUserTenantRelSaveVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否默认员工;[0-否 1-是]
     */
    @Schema(description = "是否默认员工")
    @NotNull(message = "请填写是否默认员工")
    private Boolean isDefault;
    /**
     * 用户
     */
    @Schema(description = "用户")
    @NotNull(message = "请填写用户")
    private Long userId;
    /**
     * 状态;[0-禁用 1-启用]
     */
    @Schema(description = "状态")
    private Boolean state;
    /**
     * 所属企业
     */
    @Schema(description = "所属企业")
    @NotNull(message = "请填写所属企业")
    private Long tenantId;

}
