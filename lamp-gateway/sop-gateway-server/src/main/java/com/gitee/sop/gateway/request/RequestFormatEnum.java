package com.gitee.sop.gateway.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 六如
 */
@Getter
@AllArgsConstructor
public enum RequestFormatEnum {
    NONE(""),
    JSON("json"),
    XML("xml");

    private final String value;

    public static RequestFormatEnum of(String value) {
        for (RequestFormatEnum requestFormatEnum : RequestFormatEnum.values()) {
            if (requestFormatEnum.value.equalsIgnoreCase(value)) {
                return requestFormatEnum;
            }
        }
        return NONE;
    }
}
