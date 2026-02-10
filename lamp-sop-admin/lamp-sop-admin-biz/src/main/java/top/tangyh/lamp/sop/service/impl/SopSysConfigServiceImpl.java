package top.tangyh.lamp.sop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.sop.entity.SopSysConfig;
import top.tangyh.lamp.sop.manager.SopSysConfigManager;
import top.tangyh.lamp.sop.service.SopSysConfigService;

/**
 * <p>
 * 业务实现类
 * 系统配置表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:44
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SopSysConfigServiceImpl extends SuperServiceImpl<SopSysConfigManager, Long, SopSysConfig> implements SopSysConfigService {


}


