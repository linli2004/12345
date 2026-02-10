package top.tangyh.lamp.test.manager2;

import top.tangyh.basic.base.manager.SuperCacheManager;
import top.tangyh.lamp.test.entity.DefGenTestSimple;

/**
 * <p>
 * 通用业务接口
 * 测试单表
 * </p>
 *
 * @author zuihou
 * @date 2022-04-15 15:36:45
 * @create [2022-04-15 15:36:45] [zuihou] [代码生成器生成]
 */
public interface SimpleCacheManager extends SuperCacheManager<DefGenTestSimple> {
    /**
     * 插入数据
     *
     * @param defGenTestSimple 参数
     */
    void insert(DefGenTestSimple defGenTestSimple);
}


