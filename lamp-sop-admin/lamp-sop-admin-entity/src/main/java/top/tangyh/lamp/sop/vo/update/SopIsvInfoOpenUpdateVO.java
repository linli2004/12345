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

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 申请
 * isv信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-11 10:51:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "isv申请")
public class SopIsvInfoOpenUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "ID")
    @NotNull(message = "请填写ID")
    private Long id;
    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotEmpty(message = "请填写名称")
    @Size(max = 255, message = "名称长度不能超过{max}")
    private String name;


    /**
     * 备注
     */
    @Schema(description = "备注")
    @Size(max = 512, message = "备注长度不能超过{max}")
    private String remark;

    /**
     * 回调接口
     */
    @Schema(description = "回调接口")
    @Size(max = 255, message = "回调接口长度不能超过{max}")
    private String notifyUrl;


}
