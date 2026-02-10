package top.tangyh.lamp.base.manager.common;

import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.lamp.base.entity.common.BaseDict;
import top.tangyh.lamp.system.vo.result.system.DefDictItemResultVO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 字典管理
 *
 * @author tangyh
 * @version v1.0
 * @date 2021/9/29 1:26 下午
 * @create [2021/9/29 1:26 下午 ] [tangyh] [初始创建]
 */
public interface BaseDictManager extends SuperManager<BaseDict> {

    Map<Serializable, BaseDict> findByIds(Set<Serializable> dictKeys);

    /**
     * 根据字典key查询系统默认的字典条目
     *
     * @param dictKeys 字典key
     * @return key： 字典key  value: item list
     */
    Map<String, List<DefDictItemResultVO>> findDictMapItemListByKey(List<String> dictKeys);
}
