package top.tangyh.lamp.sop.event;

import java.util.Collection;

/**
 * isv 变更事件
 * @param isvIds isv集合
 * @author zuihou
 */
public record ChangeIsvInfoEvent(Collection<Long> isvIds) {

}
