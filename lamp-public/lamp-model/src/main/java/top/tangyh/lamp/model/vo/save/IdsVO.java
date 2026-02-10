package top.tangyh.lamp.model.vo.save;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 通用 id 表单对象
 * @author zuihou
 */
@Data
public class IdsVO {
    @NotEmpty(message = "请填写主键")
    private List<Long> ids;
}
