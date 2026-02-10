package top.tangyh.lamp.test.manager.impl;

import cn.hutool.core.util.RandomUtil;
import top.tangyh.basic.base.entity.SuperEntity;
import top.tangyh.basic.model.cache.CacheKey;
import top.tangyh.basic.model.cache.CacheKeyBuilder;
import top.tangyh.lamp.common.cache.CacheKeyModular;

import java.time.Duration;

public class DefGenTestSimpleBuilder implements CacheKeyBuilder {
    public static CacheKey builder2(String name, Long id) {
        return new DefGenTestSimpleBuilder().key(name, id);
    }

    public CacheKey builder(String name, Long id) {
        return this.key(name, id);
    }

    public CacheKey builder(Long id) {
        return this.key(id);
    }



    @Override
    public String getTable() {
//        return CacheKeyTable.Base.BASE_DICT;
        return "def_simple";
    }

    @Override
    public String getModular() {
        return CacheKeyModular.BASE;
    }

    @Override
    public String getField() {
        return SuperEntity.ID_FIELD;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.obj;
    }

    @Override
    public Duration getExpire() {
        // 缓存同时失效导致的
        return Duration.ofSeconds(RandomUtil.randomInt(7200, 7500));
//        return Duration.ofHours(RandomUtil.randomInt(3, 5));
//        return Duration.ofHours(2);
    }
}
