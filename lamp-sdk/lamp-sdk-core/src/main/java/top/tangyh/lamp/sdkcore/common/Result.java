package top.tangyh.lamp.sdkcore.common;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import top.tangyh.lamp.sdkcore.util.StringUtils;

/**
 * @author 六如
 */
@Data
public class Result<T> {
    private String code;
    private String msg;
    private String subCode;
    private String subMsg;
    private T data;

    @JSONField(serialize = false)
    public boolean isSuccess() {
        return StringUtils.isEmpty(subCode);
    }

}
