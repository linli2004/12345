package top.tangyh.lamp.system.vo.save.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author zuihou
 * @date 2021/11/7 20:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(title = "DefTenantBindUserVO", description = "企业绑定用户")
public class DefTenantBindUserVO {

    /**
     * 公司id
     */
    @Schema(description = "公司id")
    private Long tenantId;

    /**
     * 用户id列表
     */
    @Schema(description = "用户id列表")
    private List<Long> userIdList;

    private Boolean isBind;

    private String reviewComments;
}
