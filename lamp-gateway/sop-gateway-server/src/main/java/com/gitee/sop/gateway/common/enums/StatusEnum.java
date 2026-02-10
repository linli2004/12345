package com.gitee.sop.gateway.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 六如
 */
@AllArgsConstructor
@Getter
public enum StatusEnum {
    NONE(0),
    ENABLE(1),
    DISABLE(2);

    private final int value;

    public static StatusEnum of(Number number) {
        if (number == null) {
            return NONE;
        }
        for (StatusEnum value : StatusEnum.values()) {
            if (value.value == number.intValue()) {
                return value;
            }
        }
        return NONE;
    }
}
