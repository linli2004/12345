package top.tangyh.lamp.base.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.base.entity.RoleStatusMapping;
import top.tangyh.lamp.base.manager.RoleStatusMappingManager;
import top.tangyh.lamp.base.service.RoleStatusMappingService;
import top.tangyh.lamp.common.constant.DsConstant;

/**
 * <p>
 * 业务实现类
 * 角色视图映射配置表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:49:32
 * @create [2026-03-03 11:49:32] [lunar] [代码生成器生成]
 */
@DS(DsConstant.BASE_TENANT)
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoleStatusMappingServiceImpl extends SuperServiceImpl<RoleStatusMappingManager, Long, RoleStatusMapping> implements RoleStatusMappingService {


}


