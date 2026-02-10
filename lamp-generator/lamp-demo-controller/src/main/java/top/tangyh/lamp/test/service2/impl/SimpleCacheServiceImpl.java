package top.tangyh.lamp.test.service2.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.service.impl.SuperCacheServiceImpl;
import top.tangyh.lamp.test.entity.DefGenTestSimple;
import top.tangyh.lamp.test.manager2.SimpleCacheManager;
import top.tangyh.lamp.test.service2.SimpleCacheService;


/**
 * <p>
 * 业务接口
 * 测试单表
 * </p>
 *
 * @author zuihou
 * @date 2022-04-15 15:36:45
 * @create [2022-04-15 15:36:45] [zuihou] [代码生成器生成]
 */
@Service
@RequiredArgsConstructor
public class SimpleCacheServiceImpl extends SuperCacheServiceImpl<SimpleCacheManager, Long, DefGenTestSimple>
        implements SimpleCacheService {

}


