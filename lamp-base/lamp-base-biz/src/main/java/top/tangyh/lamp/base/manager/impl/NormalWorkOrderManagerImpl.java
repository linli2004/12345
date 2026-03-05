package top.tangyh.lamp.base.manager.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.manager.NormalWorkOrderManager;
import top.tangyh.lamp.base.mapper.NormalWorkOrderMapper;
import top.tangyh.lamp.base.vo.query.NormalWorkOrderPageQuery;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderResultVO;

/**
 * <p>
 * 通用业务实现类
 * 普通工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:47:40
 * @create [2026-03-03 11:47:40] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NormalWorkOrderManagerImpl extends SuperManagerImpl<NormalWorkOrderMapper, NormalWorkOrder> implements NormalWorkOrderManager {

    @Override
    public IPage<NormalWorkOrderResultVO> selectPageResultVO(IPage<NormalWorkOrder> page, Wrapper<NormalWorkOrder> wrapper, NormalWorkOrderPageQuery model) {
        return baseMapper.selectPageResultVO(page, wrapper, model);
    }
}


