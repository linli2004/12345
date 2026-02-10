package top.tangyh.lamp.system.service.application.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.system.entity.application.DefTenantApplicationRecord;
import top.tangyh.lamp.system.manager.application.DefTenantApplicationRecordManager;
import top.tangyh.lamp.system.service.application.DefTenantApplicationRecordService;

/**
 * <p>
 * 业务实现类
 * 租户应用授权记录
 * </p>
 *
 * @author zuihou
 * @date 2021-09-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@DS(DsConstant.DEFAULTS)
public class DefTenantApplicationRecordServiceImpl extends SuperServiceImpl<DefTenantApplicationRecordManager, Long, DefTenantApplicationRecord>
        implements DefTenantApplicationRecordService {
}
