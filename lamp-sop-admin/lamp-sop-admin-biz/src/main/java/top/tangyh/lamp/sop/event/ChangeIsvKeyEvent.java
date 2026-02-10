package top.tangyh.lamp.sop.event;

import java.util.List;

/**
 * ISV 公私钥变更事件
 * @param isvIds ISV集合
 * @author zuihou
 */
public record ChangeIsvKeyEvent(List<Long> isvIds) {

}
