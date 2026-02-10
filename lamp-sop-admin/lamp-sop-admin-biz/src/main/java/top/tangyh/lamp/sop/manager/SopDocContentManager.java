package top.tangyh.lamp.sop.manager;

import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.lamp.sop.entity.SopDocContent;

/**
 * <p>
 * 通用业务接口
 * 文档内容
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
public interface SopDocContentManager extends SuperManager<SopDocContent> {
    void saveContent(Long docInfoId, String content);
}


