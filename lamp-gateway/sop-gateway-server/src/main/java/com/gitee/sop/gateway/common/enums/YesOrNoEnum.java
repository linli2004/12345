package com.gitee.sop.gateway.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author 六如
 */
@AllArgsConstructor
@Getter
public enum YesOrNoEnum {
    YES(1),
    NO(0);

    private final int value;

    public static YesOrNoEnum of(Integer value) {
        return Objects.equals(value, YES.value) ? YES : NO;
    }

    public static YesOrNoEnum of(Boolean value) {
        return Objects.equals(value, true) ? YES : NO;
    }
}
