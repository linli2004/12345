package top.tangyh.lamp.tenant.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tangyh.lamp.common.constant.DsConstant;

/**
 * @author zuihou
 * @date 2020/12/30 8:57 下午
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DatasourceInitDataSourceService {
    private final DataSourceService dataSourceService;

    /**
     * 启动项目时，调用初始化数据源
     */
    @DS(DsConstant.DEFAULTS)
    public void initDataSource() {
        dataSourceService.loadSystemDataSource();
        dataSourceService.loadCustomDataSource();
        log.debug("初始化租户数据源成功");
    }

}
