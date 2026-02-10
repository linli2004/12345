package top.tangyh.lamp.base.manager.common;

import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.basic.interfaces.echo.LoadService;
import top.tangyh.lamp.base.entity.common.BaseParameter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通用业务接口
 * 个性参数
 * </p>
 *
 * @author zuihou
 * @date 2021-11-08
 */
public interface BaseParameterManager extends SuperManager<BaseParameter>, LoadService {
    /**
     * 根据参数key查参数值
     * <p>
     * 1. 先查询租户自己的参数。
     * 2. 若不存在，则查询系统默认的参数。
     *
     * @param paramsKeys 参数key
     * @return key： 参数key  value: 参数值
     */
    Map<String, String> findParamMapByKey(List<String> paramsKeys);
}
