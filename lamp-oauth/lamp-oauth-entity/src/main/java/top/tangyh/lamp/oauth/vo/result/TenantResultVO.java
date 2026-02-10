package top.tangyh.lamp.oauth.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.tangyh.lamp.base.entity.user.BaseOrg;
import top.tangyh.lamp.system.vo.result.tenant.DefTenantResultVO;

import java.util.List;

/**
 * 租户信息
 *
 * @author tangyh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "租户扩展")
public class TenantResultVO extends DefTenantResultVO {
    @Schema(description = "当前租户下，所属单位或部门")
    private List<BaseOrg> orgList;
}
