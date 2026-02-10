package top.tangyh.lamp.system.manager.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.system.entity.application.DefTenantApplicationRecord;
import top.tangyh.lamp.system.manager.application.DefTenantApplicationRecordManager;
import top.tangyh.lamp.system.mapper.application.DefTenantApplicationRecordMapper;

/**
 * 应用管理
 *
 * @author tangyh
 * @version v1.0
 * @date 2021/9/29 1:26 下午
 * @create [2021/9/29 1:26 下午 ] [tangyh] [初始创建]
 */
@RequiredArgsConstructor
@Service
public class DefTenantApplicationRecordManagerImpl extends SuperManagerImpl<DefTenantApplicationRecordMapper, DefTenantApplicationRecord> implements DefTenantApplicationRecordManager {

}
