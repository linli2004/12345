package top.tangyh.lamp.model.vo.save;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 通用 状态 修改对象
 * @author zuihou
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StatusUpdateVO extends IdVO {

    @NotNull(message = "状态不能为空")
    private Integer status;

}
