package top.tangyh.lamp.sop.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.sop.entity.SopNotifyInfo;
import top.tangyh.lamp.sop.manager.SopNotifyInfoManager;
import top.tangyh.lamp.sop.mapper.SopNotifyInfoMapper;

/**
 * <p>
 * 通用业务实现类
 * 回调信息
 * </p>
 *
 * @author zuihou
 * @date 2025-12-17 15:38:07
 * @create [2025-12-17 15:38:07] [zuihou] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SopNotifyInfoManagerImpl extends SuperManagerImpl<SopNotifyInfoMapper, SopNotifyInfo> implements SopNotifyInfoManager {
}


