package top.tangyh.lamp.system.service.tenant.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.system.entity.tenant.DefDatasourceConfig;
import top.tangyh.lamp.system.manager.tenant.DefDatasourceConfigManager;
import top.tangyh.lamp.system.service.tenant.DefDatasourceConfigService;
import top.tangyh.lamp.tenant.service.DataSourceService;

/**
 * <p>
 * 业务实现类
 * 数据源
 * </p>
 *
 * @author zuihou
 * @date 2021-09-13
 */
@Slf4j
@Service
@DS(DsConstant.DEFAULTS)
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefDatasourceConfigServiceImpl extends SuperServiceImpl<DefDatasourceConfigManager, Long, DefDatasourceConfig>
        implements DefDatasourceConfigService {
    private final DataSourceService dataSourceService;

    @Override
    public Boolean testConnection(Long id) {
        DefDatasourceConfig config = superManager.getById(id);
        DataSourceProperty dataSourceProperty = BeanUtil.toBean(config, DataSourceProperty.class);
        return dataSourceService.testConnection(dataSourceProperty);
    }
}
