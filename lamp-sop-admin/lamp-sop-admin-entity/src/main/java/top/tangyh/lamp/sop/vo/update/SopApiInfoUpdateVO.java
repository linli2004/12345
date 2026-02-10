package top.tangyh.lamp.sop.vo.update;

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
import top.tangyh.basic.base.entity.SuperEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 表单修改方法VO
 * 接口信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-11 10:34:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "接口信息表")
public class SopApiInfoUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @NotNull(message = "请填写id", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    @NotEmpty(message = "请填写应用名称")
    @Size(max = 64, message = "应用名称长度不能超过{max}")
    private String application;
    /**
     * 接口名称
     */
    @Schema(description = "接口名称")
    @NotEmpty(message = "请填写接口名称")
    @Size(max = 128, message = "接口名称长度不能超过{max}")
    private String apiName;
    /**
     * 版本号
     */
    @Schema(description = "版本号")
    @NotEmpty(message = "请填写版本号")
    @Size(max = 16, message = "版本号长度不能超过{max}")
    private String apiVersion;
    /**
     * 接口描述
     */
    @Schema(description = "接口描述")
    @Size(max = 64, message = "接口描述长度不能超过{max}")
    private String description;
    /**
     * 备注
     */
    @Schema(description = "备注")
    @Size(max = 65535, message = "备注长度不能超过{max}")
    private String remark;
    /**
     * 接口class
     */
    @Schema(description = "接口class")
    @NotEmpty(message = "请填写接口class")
    @Size(max = 128, message = "接口class长度不能超过{max}")
    private String interfaceClassName;
    /**
     * 方法名称
     */
    @Schema(description = "方法名称")
    @NotEmpty(message = "请填写方法名称")
    @Size(max = 128, message = "方法名称长度不能超过{max}")
    private String methodName;
    /**
     * 参数信息
     */
    @Schema(description = "参数信息")
    @Size(max = 65535, message = "参数信息长度不能超过{max}")
    private String paramInfo;
    /**
     * 接口是否需要授权访问
     */
    @Schema(description = "接口是否需要授权访问")
    @NotNull(message = "请填写接口是否需要授权访问")
    private Integer isPermission;
    /**
     * 是否需要appAuthToken
     */
    @Schema(description = "是否需要appAuthToken")
    @NotNull(message = "请填写是否需要appAuthToken")
    private Integer isNeedToken;
    /**
     * 是否有公共响应参数
     */
    @Schema(description = "是否有公共响应参数")
    private Integer hasCommonResponse;
    /**
     * 注册来源
     * [1-系统注册 2-手动注册]
     */
    @Schema(description = "注册来源")
    @NotNull(message = "请填写注册来源")
    private Integer regSource;
    /**
     * 接口模式
     * [1-open接口 2-Restful模式]
     */
    @Schema(description = "接口模式")
    private Integer apiMode;
    /**
     * 状态
     * [1-启用 0-禁用]
     */
    @Schema(description = "状态")
    @NotNull(message = "请填写状态")
    private Integer status;


}
