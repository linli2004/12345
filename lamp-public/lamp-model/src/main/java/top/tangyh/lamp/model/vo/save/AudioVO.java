package top.tangyh.lamp.model.vo.save;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 审核
 * @author tangyh
 * @since 2025/5/12 09:22
 */
@Data
public class AudioVO {
    @NotNull(message = "请填写ID")
    private Long id;

    /** 审核状态  0-初始化  1-申请中 2-通过  99-退回 */
    @NotNull(message = "请填写审核状态")
    private Integer auditStatus;

    /** 审核意见 */
    private String reviewComments;
}
