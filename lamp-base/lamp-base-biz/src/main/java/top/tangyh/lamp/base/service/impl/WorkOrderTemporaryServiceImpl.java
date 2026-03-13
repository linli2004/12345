package top.tangyh.lamp.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.base.entity.WorkOrderTemporary;
import top.tangyh.lamp.base.manager.WorkOrderTemporaryManager;
import top.tangyh.lamp.base.service.WorkOrderTemporaryService;
import top.tangyh.lamp.common.constant.DsConstant;

/**
 * <p>
 * 业务实现类
 * 工单办理暂存
 * </p>
 *
 * @author lunar
 * @date 2026-03-12 11:50:36
 * @create [2026-03-12 11:50:36] [lunar] [代码生成器生成]
 */
@DS(DsConstant.BASE_TENANT)
@Slf4j
@RequiredArgsConstructor
@Service
public class WorkOrderTemporaryServiceImpl extends SuperServiceImpl<WorkOrderTemporaryManager, Long, WorkOrderTemporary> implements WorkOrderTemporaryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <SaveVO> WorkOrderTemporary save(SaveVO saveVO) {
        WorkOrderTemporary workOrderTemporary = BeanUtil.copyProperties(saveVO, WorkOrderTemporary.class);
        superManager.saveOrUpdate(workOrderTemporary);
        return workOrderTemporary;
    }
}


