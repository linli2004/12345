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
import top.tangyh.basic.utils.BeanPlusUtil;
import top.tangyh.lamp.base.entity.common.BaseDict;
import top.tangyh.lamp.base.manager.common.BaseDictManager;
import top.tangyh.lamp.base.service.common.BaseDictService;
import top.tangyh.lamp.base.vo.save.common.BaseDictSaveVO;
import top.tangyh.lamp.base.vo.update.common.BaseDictUpdateVO;
import top.tangyh.lamp.common.cache.base.common.BaseDictCacheKeyBuilder;
import top.tangyh.lamp.common.constant.DefValConstants;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.model.enumeration.system.DictClassifyEnum;
import top.tangyh.lamp.system.entity.system.DefDict;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 业务实现类
 * 字典
 * </p>
 *
 * @author zuihou
 * @date 2021-10-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@DS(DsConstant.BASE_TENANT)
public class BaseDictServiceImpl extends SuperServiceImpl<BaseDictManager, Long, BaseDict> implements BaseDictService {

    private final CachePlusOps cachePlusOps;

    @Override
    public boolean checkByKey(String key, Long id) {
        ArgumentAssert.notEmpty(key, "请填写字典标识");
        return superManager.count(Wraps.<BaseDict>lbQ().eq(BaseDict::getKey, key)
                .eq(BaseDict::getParentId, DefValConstants.PARENT_ID).ne(BaseDict::getId, id)) > 0;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public <SaveVO> BaseDict save(SaveVO saveVO) {
        BaseDictSaveVO dictSaveVO = (BaseDictSaveVO) saveVO;
        ArgumentAssert.isFalse(checkByKey(dictSaveVO.getKey(), null), "字典标识已存在");
        BaseDict dict = BeanPlusUtil.toBean(dictSaveVO, BaseDict.class);
        dict.setClassify(DictClassifyEnum.BUSINESS.getCode());
        dict.setParentId(DefValConstants.PARENT_ID);
        superManager.save(dict);
        return dict;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDict(List<Long> ids) {
        List<BaseDict> list = superManager.listByIds(ids);
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        if (CollUtil.isNotEmpty(ids)) {
            superManager.remove(Wraps.<BaseDict>lbQ().in(BaseDict::getParentId, ids));
        }
        boolean flag = removeByIds(ids);
        CacheHashKey[] typeKeys = list.stream().map(type -> BaseDictCacheKeyBuilder.builder(type.getKey())).toArray(CacheHashKey[]::new);
        cachePlusOps.del(typeKeys);
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <UpdateVO> BaseDict updateById(UpdateVO updateVO) {
        BaseDictUpdateVO dictUpdateVO = (BaseDictUpdateVO) updateVO;
        BaseDict dict = BeanPlusUtil.toBean(dictUpdateVO, BaseDict.class);
        dict.setParentId(DefValConstants.PARENT_ID);
        superManager.updateById(dict);
        return dict;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDictAndItem(DefDict defDict, List<DefDict> itemList) {
        BaseDict baseDict = BeanUtil.toBean(defDict, BaseDict.class);
        boolean flag = superManager.save(baseDict);
        if (!itemList.isEmpty()) {
            List<BaseDict> baseItemList = new ArrayList<>();
            itemList.forEach(item -> {
                BaseDict baseItem = new BaseDict();
                BeanUtil.copyProperties(item, baseItem);
                baseItem.setParentId(baseDict.getId());
                baseItemList.add(baseItem);
            });

            superManager.saveBatch(baseItemList);
            itemList.forEach(item -> {
                // 删除空值
                cachePlusOps.hDel(BaseDictCacheKeyBuilder.builder(item.getParentKey(), DefValConstants.DICT_NULL_VAL_KEY));

                CacheHashKey hashKey = BaseDictCacheKeyBuilder.builder(item.getParentKey(), item.getKey());
                cachePlusOps.hSet(hashKey, item.getName());
            });
        }
        return flag;
    }
}
