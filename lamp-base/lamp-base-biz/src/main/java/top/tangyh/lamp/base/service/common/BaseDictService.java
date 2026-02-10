package top.tangyh.lamp.base.service.common;

import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.base.entity.common.BaseDict;
import top.tangyh.lamp.system.entity.system.DefDict;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 字典
 * </p>
 *
 * @author zuihou
 * @date 2021-10-08
 */
public interface BaseDictService extends SuperService<Long, BaseDict> {

    /**
     * 检查字典标识是否可用
     *
     * @param key 标识
     * @param id  当前id
     * @return
     */
    boolean checkByKey(String key, Long id);

    /**
     * 删除字典
     *
     * @param ids
     * @return
     */
    Boolean deleteDict(List<Long> ids);

    /**
     * 将系统字典导入到个性字典
     *
     * @param defDict  系统字典
     * @param itemList 系统字典项
     * @return java.lang.Boolean
     * @author tangyh
     * @date 2021/10/9 3:41 下午
     * @create [2021/10/9 3:41 下午 ] [tangyh] [初始创建]
     */
    Boolean saveDictAndItem(DefDict defDict, List<DefDict> itemList);
}
