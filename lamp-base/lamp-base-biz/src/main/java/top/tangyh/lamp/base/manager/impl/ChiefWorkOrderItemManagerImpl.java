package top.tangyh.lamp.base.manager.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.base.entity.ChiefWorkOrderItem;
import top.tangyh.lamp.base.manager.ChiefWorkOrderItemManager;
import top.tangyh.lamp.base.mapper.ChiefWorkOrderItemMapper;
import top.tangyh.lamp.base.vo.query.ChiefWorkOrderItemPageQuery;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderItemResultVO;

import java.util.List;

/**
 * <p>
 * 通用业务实现类
 * 督办工单详情
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChiefWorkOrderItemManagerImpl extends SuperManagerImpl<ChiefWorkOrderItemMapper, ChiefWorkOrderItem> implements ChiefWorkOrderItemManager {

    @Override
    public IPage<ChiefWorkOrderItemResultVO> selectPageResultVO(IPage<ChiefWorkOrderItem> page, Wrapper<ChiefWorkOrderItem> wrapper, ChiefWorkOrderItemPageQuery model) {
        return baseMapper.selectPageResultVO(page, wrapper, model);
    }

    @Override
    public List<ChiefWorkOrderItemResultVO> selectListResultVO(ChiefWorkOrderItemPageQuery model) {
        return baseMapper.selectListResultVO(model);
    }

    @Override
    public IPage<ChiefWorkOrderItemResultVO> selectCommentedResultVO(IPage<ChiefWorkOrderItem> page, Wrapper<ChiefWorkOrderItem> wrapper, ChiefWorkOrderItemPageQuery model) {
        return baseMapper.selectCommentedResultVO(page, wrapper, model);
    }
}
