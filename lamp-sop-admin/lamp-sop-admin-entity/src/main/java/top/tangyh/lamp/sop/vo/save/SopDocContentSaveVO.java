package top.tangyh.lamp.sop.vo.save;

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

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 表单保存方法VO
 * 文档内容
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
@Schema(description = "文档内容")
public class SopDocContentSaveVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文档ID
     * doc_info.id
     */
    @Schema(description = "文档ID doc_info.id")
    @NotNull(message = "请填写文档ID doc_info.id")
    private Long docInfoId;
    /**
     * 文档内容
     */
    @Schema(description = "文档内容")
    @Size(max = 2147483647, message = "文档内容长度不能超过{max}")
    private String content;


}
