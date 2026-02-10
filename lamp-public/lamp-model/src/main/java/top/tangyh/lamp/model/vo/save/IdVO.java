package top.tangyh.lamp.model.vo.save;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 通用 id 表单对象
 * @author zuihou
 */
@Data
public class IdVO {
    @NotNull(message = "请填写主键")
    private Long id;
}
