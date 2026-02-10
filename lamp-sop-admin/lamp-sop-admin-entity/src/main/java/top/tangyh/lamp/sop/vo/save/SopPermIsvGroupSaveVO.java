package top.tangyh.lamp.sop.vo.save;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 表单保存方法VO
 * isv分组
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
@Schema(description = "isv分组")
public class SopPermIsvGroupSaveVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ISV
     */
    @Schema(description = "ISV")
    @NotNull(message = "请填写ISV")
    private Long isvId;
    /**
     * 分组
     * perm_group.id
     */
    @Schema(description = "分组")
    private List<Long> groupIdList;


}
