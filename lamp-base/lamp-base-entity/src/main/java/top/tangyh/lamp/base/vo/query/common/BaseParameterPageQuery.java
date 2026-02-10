package top.tangyh.lamp.base.vo.query.common;

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
 * 个性参数
 * </p>
 *
 * @author zuihou
 * @since 2021-11-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(title = "BaseParameterPageQuery", description = "个性参数")
public class BaseParameterPageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 参数键
     */
    @Schema(description = "参数键")
    private String key;
    /**
     * 参数值
     */
    @Schema(description = "参数值")
    private String value;
    /**
     * 参数名称
     */
    @Schema(description = "参数名称")
    private String name;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remarks;
    /**
     * 状态
     */
    @Schema(description = "状态")
    private Boolean state;
    /**
     * 类型;[10-系统参数 20-业务参数]@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.System.PARAMETER_TYPE)
     */
    @Schema(description = "类型")
    private String paramType;
    /**
     * 创建人组织
     */
    @Schema(description = "创建人组织")
    private Long createdOrgId;

}
