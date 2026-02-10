package top.tangyh.lamp.base.service.common.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.cache.repository.CachePlusOps;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.model.cache.CacheHashKey;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.base.entity.common.BaseDict;
import top.tangyh.lamp.base.manager.common.BaseDictManager;
import top.tangyh.lamp.base.service.common.BaseDictItemService;
import top.tangyh.lamp.base.vo.save.common.BaseDictItemSaveVO;
import top.tangyh.lamp.base.vo.update.common.BaseDictItemUpdateVO;
import top.tangyh.lamp.common.cache.base.common.BaseDictCacheKeyBuilder;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.model.enumeration.system.DictClassifyEnum;

import java.util.Collection;
import java.util.List;

/**
 * @author zuihou
 * @date 2021/10/10 23:14
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@DS(DsConstant.BASE_TENANT)
public class BaseDictItemServiceImpl extends SuperServiceImpl<BaseDictManager, Long, BaseDict> implements BaseDictItemService {

    private final CachePlusOps cachePlusOps;

    @Override
    public boolean checkItemByKey(String key, Long dictId, Long id) {
        ArgumentAssert.notEmpty(key, "请填写字典项标识");
        ArgumentAssert.notNull(dictId, "字典不能为空");
        return superManager.count(Wraps.<BaseDict>lbQ().eq(BaseDict::getKey, key)
                .eq(BaseDict::getParentId, dictId).ne(BaseDict::getId, id)) > 0;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public <SaveVO> BaseDict save(SaveVO saveVO) {
        BaseDictItemSaveVO itemSaveVO = (BaseDictItemSaveVO) saveVO;
        BaseDict model = BeanUtil.toBean(itemSaveVO, BaseDict.class);
        ArgumentAssert.isFalse(checkItemByKey(model.getKey(), model.getParentId(), null), "字典[{}]已经存在，请勿重复创建", model.getKey());
        BaseDict parent = getById(model.getParentId());
        ArgumentAssert.notNull(parent, "字典不存在");
        model.setParentKey(parent.getKey());
        model.setClassify(DictClassifyEnum.BUSINESS.getCode());
        superManager.save(model);
        CacheHashKey hashKey = BaseDictCacheKeyBuilder.builder(model.getParentKey(), model.getKey());
        cachePlusOps.hSet(hashKey, model.getName());
        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <UpdateVO> BaseDict updateById(UpdateVO updateVO) {
        BaseDictItemUpdateVO itemUpdateVO = (BaseDictItemUpdateVO) updateVO;
        BaseDict model = BeanUtil.toBean(itemUpdateVO, BaseDict.class);
        BaseDict parent = getById(model.getParentId());
        ArgumentAssert.notNull(parent, "字典不存在");
        model.setParentKey(parent.getKey());
        superManager.updateById(model);
        CacheHashKey hashKey = BaseDictCacheKeyBuilder.builder(model.getParentKey(), model.getKey());
        cachePlusOps.hSet(hashKey, model.getName());
        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<Long> idList) {
        List<BaseDict> list = superManager.listByIds(idList);
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        boolean flag = superManager.removeByIds(idList);

        CacheHashKey[] hashKeys = list.stream().map(model -> BaseDictCacheKeyBuilder.builder(model.getParentKey(), model.getKey())).toArray(CacheHashKey[]::new);
        cachePlusOps.del(hashKeys);
        return flag;
    }

}
