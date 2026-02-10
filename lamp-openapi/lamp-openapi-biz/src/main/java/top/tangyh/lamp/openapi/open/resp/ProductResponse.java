package top.tangyh.lamp.openapi.open.resp;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 六如
 */
@Data
public class ProductResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -3743413007549072654L;

    private Integer id;

    private String name;

    // 日期格式要用Date,暂不支持LocalDateTime
    private Date addTime = new Date();
}
