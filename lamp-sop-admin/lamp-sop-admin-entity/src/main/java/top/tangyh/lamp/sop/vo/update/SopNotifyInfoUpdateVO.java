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
import java.time.LocalDateTime;

/**
 * <p>
 * 表单修改方法VO
 * 回调信息
 * </p>
 *
 * @author zuihou
 * @date 2025-12-17 15:38:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "回调信息")
public class SopNotifyInfoUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "")
    @NotNull(message = "请填写", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * app_id
     */
    @Schema(description = "app_id")
    @NotEmpty(message = "请填写app_id")
    @Size(max = 100, message = "app_id长度不能超过{max}")
    private String appId;
    /**
     * api_name
     */
    @Schema(description = "api_name")
    @NotEmpty(message = "请填写api_name")
    @Size(max = 128, message = "api_name长度不能超过{max}")
    private String apiName;
    /**
     * api_version
     */
    @Schema(description = "api_version")
    @NotEmpty(message = "请填写api_version")
    @Size(max = 16, message = "api_version长度不能超过{max}")
    private String apiVersion;
    /**
     * 回调url
     */
    @Schema(description = "回调url")
    @Size(max = 255, message = "回调url长度不能超过{max}")
    private String notifyUrl;
    /**
     * 最近一次发送时间
     */
    @Schema(description = "最近一次发送时间")
    private LocalDateTime lastSendTime;
    /**
     * 下一次发送时间
     */
    @Schema(description = "下一次发送时间")
    private LocalDateTime nextSendTime;
    /**
     * 最大发送次数
     */
    @Schema(description = "最大发送次数")
    private Integer sendMax;
    /**
     * 已发送次数
     */
    @Schema(description = "已发送次数")
    private Integer sendCnt;
    /**
     * 发送内容
     */
    @Schema(description = "发送内容")
    @Size(max = 65535, message = "发送内容长度不能超过{max}")
    private String content;
    /**
     * 状态
     * [1-发送成功,2-发送失败,3-重试结束]
     */
    @Schema(description = "状态")
    private String notifyStatus;
    /**
     * 失败原因
     */
    @Schema(description = "失败原因")
    @Size(max = 65535, message = "失败原因长度不能超过{max}")
    private String errorMsg;
    /**
     * 返回结果
     */
    @Schema(description = "返回结果")
    @Size(max = 65535, message = "返回结果长度不能超过{max}")
    private String resultContent;
    /**
     * 备注
     */
    @Schema(description = "备注")
    @Size(max = 256, message = "备注长度不能超过{max}")
    private String remark;
    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Long tenantId;


}
