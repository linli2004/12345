package top.tangyh.lamp.oauth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.lamp.base.manager.common.BaseParameterManager;
import top.tangyh.lamp.oauth.service.ParamService;
import top.tangyh.lamp.system.manager.system.DefParameterManager;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zuihou
 * @date 2021/10/7 13:27
 */
@Service
@RequiredArgsConstructor
public class ParamServiceImpl implements ParamService {
    private final BaseParameterManager baseParameterManager;
    private final DefParameterManager defParameterManager;

    /**
     * 先从base库查， 若base库没有，在去def库查。
     * 若2个库都有，采用base库的数据
     *
     * @param paramsKeys 字典key
     * @return
     */
    @Override
    public Map<String, String> findParamMapByKey(List<String> paramsKeys) {
        if (CollUtil.isEmpty(paramsKeys)) {
            return Collections.emptyMap();
        }

        if (ContextUtil.isEmptyTenantId()) {
            return defParameterManager.findParamMapByKey(paramsKeys);
        }

        Map<String, String> baseMap = baseParameterManager.findParamMapByKey(paramsKeys);
//        dictKeys 数量和 baseMap.key 数量相同，说明所有的字典在base库都自定义了
        if (baseMap != null && baseMap.keySet().size() == paramsKeys.size()) {
            return baseMap;
        }

        // 查询不在base的参数
        List<String> nonExistKeys = paramsKeys.stream().filter(dictKey -> !baseMap.containsKey(dictKey)).toList();
        Map<String, String> defMap = defParameterManager.findParamMapByKey(nonExistKeys);

        Map<String, String> map = MapUtil.newHashMap();
        map.putAll(defMap);
        map.putAll(baseMap);
        return map;
    }


    @Override
    public Map<Serializable, Object> findByIds(Set<Serializable> paramKeys) {
        if (CollUtil.isEmpty(paramKeys)) {
            return Collections.emptyMap();
        }

        if (ContextUtil.isEmptyTenantId()) {
            return defParameterManager.findByIds(paramKeys);
        }

        Map<Serializable, Object> baseMap = baseParameterManager.findByIds(paramKeys);
//        dictKeys 数量和 baseMap.key 数量相同，说明所有的参数在base库都自定义了
        if (baseMap != null && baseMap.keySet().size() == paramKeys.size()) {
            return baseMap;
        }

        // 查询不在base的参数
        Set<Serializable> nonExistKeys = paramKeys.stream().filter(dictKey -> !baseMap.containsKey(dictKey)).collect(Collectors.toSet());
        Map<Serializable, Object> defMap = defParameterManager.findByIds(nonExistKeys);

        HashMap<Serializable, Object> map = MapUtil.newHashMap();
        map.putAll(defMap);
        map.putAll(baseMap);

        return map;
    }
}
