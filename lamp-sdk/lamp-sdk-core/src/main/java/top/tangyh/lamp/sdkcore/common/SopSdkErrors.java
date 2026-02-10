package top.tangyh.lamp.sdkcore.common;

import lombok.Getter;

/**
 * @author 六如
 */
@Getter
public enum SopSdkErrors {
    /**
     * 网络错误
     */
    HTTP_ERROR("836875001", "网络错误"),
    /**
     * 验证返回sign错误
     */
    CHECK_RESPONSE_SIGN_ERROR("836875002", "验证服务端sign出错");

    private final String code;
    private final String msg;
    private final String subCode;
    private final String subMsg;

    SopSdkErrors(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.subCode = code;
        this.subMsg = msg;
    }

    public <T> Result<T> getErrorResult() {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setSubCode(subCode);
        result.setSubMsg(subMsg);
        result.setMsg(msg);
        return result;
    }

}
