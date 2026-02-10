package top.tangyh.lamp.base.service.common;

import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.base.entity.common.BaseDict;

/**
 * @author zuihou
 * @date 2021/10/10 23:14
 */
public interface BaseDictItemService extends SuperService<Long, BaseDict> {

    /**
     * 检查字典项标识是否可用
     *
     * @param key    标识
     * @param dictId 所属字典id
     * @param id     当前id
     * @return
     */
    boolean checkItemByKey(String key, Long dictId, Long id);


}
