package top.tangyh.lamp.sop.event;

import java.util.Collection;

/**
 * 文档改变事件
 * @param apiIds 接口id
 * @author zuihou
 */
public record ChangeApiInfoEvent(Collection<Long> apiIds) {

}
