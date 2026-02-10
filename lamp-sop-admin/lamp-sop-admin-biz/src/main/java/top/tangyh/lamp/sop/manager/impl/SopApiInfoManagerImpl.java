package top.tangyh.lamp.sop.manager.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.lamp.sop.entity.SopApiInfo;
import top.tangyh.lamp.sop.manager.SopApiInfoManager;
import top.tangyh.lamp.sop.mapper.SopApiInfoMapper;
import top.tangyh.lamp.sop.vo.query.SopApiInfoPageQuery;
import top.tangyh.lamp.sop.vo.result.SopApiInfoResultVO;

/**
 * <p>
 * 通用业务实现类
 * 接口信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SopApiInfoManagerImpl extends SuperManagerImpl<SopApiInfoMapper, SopApiInfo> implements SopApiInfoManager {
    @Override
    public IPage<SopApiInfoResultVO> groupPage(IPage<SopApiInfo> page,
                                               SopApiInfoPageQuery query) {
        Wrapper<SopApiInfo> wrapper = Wraps.<SopApiInfo>lbQ()
                .eq(SopApiInfo::getApplication, query.getApplication())
                .eq(SopApiInfo::getApiName, query.getApiName())
                .eq(SopApiInfo::getStatus, query.getStatus());
        return baseMapper.groupPage(page, wrapper, query);
    }
}


