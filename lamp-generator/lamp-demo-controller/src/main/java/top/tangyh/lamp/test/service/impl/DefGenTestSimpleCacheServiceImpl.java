package top.tangyh.lamp.test.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperCacheServiceImpl;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.test.entity.DefGenTestSimple;
import top.tangyh.lamp.test.manager.DefGenTestSimpleCacheManager;
import top.tangyh.lamp.test.service.DefGenTestSimpleCacheService;

/**
 * <p>
 * 业务实现类
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
@Transactional(readOnly = true)
@DS(DsConstant.DEFAULTS)
//@DS(DsConstant.BASE_TENANT)
public class DefGenTestSimpleCacheServiceImpl extends SuperCacheServiceImpl<DefGenTestSimpleCacheManager, Long, DefGenTestSimple> implements DefGenTestSimpleCacheService {

}


