package top.tangyh.lamp.sop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.sop.entity.SopDocContent;
import top.tangyh.lamp.sop.manager.SopDocContentManager;
import top.tangyh.lamp.sop.service.SopDocContentService;

/**
 * <p>
 * 业务实现类
 * 文档内容
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SopDocContentServiceImpl extends SuperServiceImpl<SopDocContentManager, Long, SopDocContent> implements SopDocContentService {


}


