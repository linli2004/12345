package top.tangyh.lamp.sop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.sop.entity.SopHelpDoc;
import top.tangyh.lamp.sop.manager.SopHelpDocManager;
import top.tangyh.lamp.sop.service.SopHelpDocService;

/**
 * <p>
 * 业务实现类
 * 帮助内容
 * </p>
 *
 * @author zuihou
 * @date 2025-12-18 12:21:28
 * @create [2025-12-18 12:21:28] [zuihou] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SopHelpDocServiceImpl extends SuperServiceImpl<SopHelpDocManager, Long, SopHelpDoc> implements SopHelpDocService {


}


