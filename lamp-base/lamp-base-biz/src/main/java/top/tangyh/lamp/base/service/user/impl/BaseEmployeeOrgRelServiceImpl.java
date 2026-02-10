package top.tangyh.lamp.base.service.user.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.base.entity.user.BaseEmployeeOrgRel;
import top.tangyh.lamp.base.manager.user.BaseEmployeeOrgRelManager;
import top.tangyh.lamp.base.service.user.BaseEmployeeOrgRelService;
import top.tangyh.lamp.common.constant.DsConstant;

import java.util.List;

/**
 * <p>
 * 业务实现类
 * 员工所在部门
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
public class BaseEmployeeOrgRelServiceImpl extends SuperServiceImpl<BaseEmployeeOrgRelManager, Long, BaseEmployeeOrgRel> implements BaseEmployeeOrgRelService {
    @Override
    public List<Long> findOrgIdListByEmployeeId(Long employeeId) {
        ArgumentAssert.notNull(employeeId, "员工id不能为空");
        return superManager.listObjs(Wraps.<BaseEmployeeOrgRel>lbQ().select(BaseEmployeeOrgRel::getOrgId).eq(BaseEmployeeOrgRel::getEmployeeId, employeeId), Convert::toLong);
    }
}
