package top.tangyh.lamp.openapi.open.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 六如
 */
@Data
public class ProductSaveRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -1214422742659231037L;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称必填")
    @Size(max = 64)
    private String productName;

    /**
     * 添加时间
     */
    @NotNull(message = "添加时间必填")
    private Date addTime;

}
