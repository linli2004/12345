package top.tangyh.lamp.base.manager.common.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.basic.utils.CollHelper;
import top.tangyh.lamp.base.entity.common.BaseParameter;
import top.tangyh.lamp.base.manager.common.BaseParameterManager;
import top.tangyh.lamp.base.mapper.common.BaseParameterMapper;
import top.tangyh.lamp.common.constant.DsConstant;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 通用业务实现类
 * 个性参数
 * </p>
 *
 * @author zuihou
 * @date 2021-11-08
 * @create [2021-11-08] [zuihou] [代码生成器生成]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BaseParameterManagerImpl extends SuperManagerImpl<BaseParameterMapper, BaseParameter> implements BaseParameterManager {
    @Override
    @DS(DsConstant.BASE_TENANT)
    public Map<Serializable, Object> findByIds(Set<Serializable> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return CollHelper.uniqueIndex(find(ids), BaseParameter::getId, BaseParameter::getName);
    }

    public List<BaseParameter> find(Set<Serializable> ids) {
        // 强转， 防止数据库隐式转换，  若你的id 是string类型，请勿强转
        return super.listByIds(ids.stream().filter(Objects::nonNull).map(Convert::toLong).toList());
    }

    @Override
    @DS(DsConstant.BASE_TENANT)
    public Map<String, String> findParamMapByKey(List<String> paramsKeys) {
        if (CollUtil.isEmpty(paramsKeys)) {
            return Collections.emptyMap();
        }
        LbQueryWrap<BaseParameter> query = Wraps.<BaseParameter>lbQ().in(BaseParameter::getKey, paramsKeys).eq(BaseParameter::getState, true);
        List<BaseParameter> list = super.list(query);

        //key 是类型
        return CollHelper.uniqueIndex(list, BaseParameter::getKey, BaseParameter::getValue);
    }
}
