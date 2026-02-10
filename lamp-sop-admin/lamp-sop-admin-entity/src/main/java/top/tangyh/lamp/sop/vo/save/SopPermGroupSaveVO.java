package top.tangyh.lamp.sop.vo.save;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
 * 表单保存方法VO
 * 分组表
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
@Schema(description = "分组表")
public class SopPermGroupSaveVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分组名称
     */
    @Schema(description = "分组名称")
    @NotEmpty(message = "请填写分组名称")
    @Size(max = 64, message = "分组名称长度不能超过{max}")
    private String groupName;
    /**
     * 是否删除
     */
    @Schema(description = "是否删除")
    private Long deletedAt;


}
