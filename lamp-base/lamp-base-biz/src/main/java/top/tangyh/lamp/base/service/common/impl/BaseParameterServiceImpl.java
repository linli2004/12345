package top.tangyh.lamp.base.service.common.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.base.entity.common.BaseParameter;
import top.tangyh.lamp.base.manager.common.BaseParameterManager;
import top.tangyh.lamp.base.service.common.BaseParameterService;
import top.tangyh.lamp.base.vo.save.common.BaseParameterSaveVO;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.model.enumeration.system.DataTypeEnum;

/**
 * <p>
 * 业务实现类
 * 个性参数
 * </p>
 *
 * @author zuihou
 * @date 2021-11-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@DS(DsConstant.BASE_TENANT)
public class BaseParameterServiceImpl extends SuperServiceImpl<BaseParameterManager, Long, BaseParameter> implements BaseParameterService {
    @Override
    protected <SaveVO> BaseParameter saveBefore(SaveVO saveVO) {
        BaseParameterSaveVO baseParameterSaveVO = (BaseParameterSaveVO) saveVO;
        BaseParameter baseParameter = super.saveBefore(baseParameterSaveVO);
        baseParameter.setParamType(DataTypeEnum.SYSTEM.getCode());
        return baseParameter;
    }


}
