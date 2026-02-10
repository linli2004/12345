package top.tangyh.lamp.sop.event;

import java.util.List;

/**
 * 分组改变事件
 * @param isvIds isv集合
 * @author zuihou
 */
public record ChangeIsvPermEvent(List<Long> isvIds) {

}
