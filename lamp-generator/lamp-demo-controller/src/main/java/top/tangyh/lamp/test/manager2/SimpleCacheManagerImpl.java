package top.tangyh.lamp.test.manager2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperCacheManagerImpl;
import top.tangyh.basic.model.cache.CacheKeyBuilder;
import top.tangyh.lamp.test.entity.DefGenTestSimple;
import top.tangyh.lamp.test.manager.impl.DefGenTestSimpleBuilder;
import top.tangyh.lamp.test.mapper.DefGenTestSimpleMapper;

/**
 * <p>
 * 通用业务实现类
 * 测试单表
 * </p>
 *
 * @author zuihou
 * @date 2022-04-15 15:36:45
 * @create [2022-04-15 15:36:45] [zuihou] [代码生成器生成]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleCacheManagerImpl extends SuperCacheManagerImpl<DefGenTestSimpleMapper, DefGenTestSimple> implements SimpleCacheManager {


    @Override
    protected CacheKeyBuilder cacheKeyBuilder() {
        return new DefGenTestSimpleBuilder();
    }

    @Override
    public boolean save(DefGenTestSimple model) {
        boolean save = super.save(model);

        return save;
    }

    /**
     * 自定义key
     */
    @Override
    public void insert(DefGenTestSimple defGenTestSimple) {

        baseMapper.insert(defGenTestSimple);


    }
}


