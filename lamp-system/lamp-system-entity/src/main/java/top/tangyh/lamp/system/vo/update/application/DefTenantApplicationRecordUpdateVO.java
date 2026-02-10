package top.tangyh.lamp.system.vo.update.application;

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
import top.tangyh.basic.base.entity.SuperEntity;

import java.io.Serializable;

/**
 * <p>
 * 实体类
 * 租户应用授权记录
 * </p>
 *
 * @author zuihou
 * @since 2021-09-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(title = "DefTenantApplicationRecordUpdateVO", description = "租户应用授权记录")
public class DefTenantApplicationRecordUpdateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @NotNull(message = "请填写主键", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 授权ID
     */
    @Schema(description = "授权ID")
    @NotNull(message = "请填写授权ID")
    private Long tenantApplicationRelId;
    /**
     * 企业ID
     */
    @Schema(description = "企业ID")
    @NotNull(message = "请填写企业ID")
    private Long tenantId;
    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    @NotNull(message = "请填写应用ID")
    private Long applicationId;
    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    @Size(max = 255, message = "应用名称长度不能超过{max}")
    private String applicationName;
    /**
     * 企业名称
     */
    @Schema(description = "企业名称")
    @Size(max = 255, message = "企业名称长度不能超过{max}")
    private String tenantName;
    /**
     * 操作人姓名
     */
    @Schema(description = "操作人姓名")
    @Size(max = 255, message = "操作人姓名长度不能超过{max}")
    private String operateByName;
    /**
     * 授权类型;[10-应用授权 20-应用续期 30-取消授权]
     *
     * @Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.System.APPLICATION_GRANT_TYPE)
     */
    @Schema(description = "授权类型")
    @Size(max = 2, message = "授权类型长度不能超过{max}")
    private String grantType;
}
