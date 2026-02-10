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
 * 租户应用授权记录
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
@Schema(title = "DefTenantApplicationRecordPageQuery", description = "租户应用授权记录")
public class DefTenantApplicationRecordPageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    @Schema(description = "企业ID")
    private Long tenantId;
    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    private Long applicationId;
    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    private String applicationName;
    /**
     * 企业名称
     */
    @Schema(description = "企业名称")
    private String tenantName;
    /**
     * 操作人姓名
     */
    @Schema(description = "操作人姓名")
    private String operateByName;
    /**
     * 操作类型
     */
    @Schema(description = "操作类型")
    private String operateType;

}
