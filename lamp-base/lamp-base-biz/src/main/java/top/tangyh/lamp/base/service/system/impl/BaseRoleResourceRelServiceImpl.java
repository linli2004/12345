package top.tangyh.lamp.base.service.system.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.base.entity.system.BaseRoleResourceRel;
import top.tangyh.lamp.base.manager.system.BaseRoleResourceRelManager;
import top.tangyh.lamp.base.service.system.BaseRoleResourceRelService;
import top.tangyh.lamp.common.constant.DsConstant;

/**
 * <p>
 * 业务实现类
 * 角色的资源
 * </p>
 *
 * @author zuihou
 * @date 2021-10-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@DS(DsConstant.BASE_TENANT)
public class BaseRoleResourceRelServiceImpl extends SuperServiceImpl<BaseRoleResourceRelManager, Long, BaseRoleResourceRel>
        implements BaseRoleResourceRelService {
}
