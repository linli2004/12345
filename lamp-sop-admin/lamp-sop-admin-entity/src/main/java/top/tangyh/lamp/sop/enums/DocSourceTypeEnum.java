package top.tangyh.lamp.sop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 来源类型,1-torna,2-自建
 *
 * @author zuihou
 */
@Getter
@AllArgsConstructor
public enum DocSourceTypeEnum {
    TORNA(1, "Torna"),
    CUSTOM(2, "自建");
    private final Integer value;
    private final String desc;


}
