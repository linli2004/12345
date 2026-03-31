package top.tangyh.lamp.base.manager;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.lamp.base.entity.ChiefWorkOrderItem;
import top.tangyh.lamp.base.vo.query.ChiefWorkOrderItemPageQuery;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderItemResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderRankingResultVO;
import top.tangyh.lamp.base.vo.result.SignCategoryIsNullNormalWorkOrderResultVO;

import java.util.List;

/**
 * <p>
 * 通用业务接口
 * 督办工单详情
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
public interface ChiefWorkOrderItemManager extends SuperManager<ChiefWorkOrderItem> {

    IPage<ChiefWorkOrderItemResultVO> selectPageResultVO(IPage<ChiefWorkOrderItem> page, Wrapper<ChiefWorkOrderItem> wrapper, ChiefWorkOrderItemPageQuery model);

    List<ChiefWorkOrderItemResultVO> selectListResultVO(ChiefWorkOrderItemPageQuery model);
    Integer selectCountResultVO(ChiefWorkOrderItemPageQuery model);

    IPage<ChiefWorkOrderItemResultVO> selectCommentedResultVO(IPage<ChiefWorkOrderItem> page, Wrapper<ChiefWorkOrderItem> wrapper, ChiefWorkOrderItemPageQuery model);

    IPage<ChiefWorkOrderItemResultVO> selectNotCommentResultVO(IPage<ChiefWorkOrderItem> page, Wrapper<ChiefWorkOrderItem> wrapper, ChiefWorkOrderItemPageQuery model);

    Long getWorkOrderCount(String displayStatus, String roleCode, String leadUnitId);

    List<SignCategoryIsNullNormalWorkOrderResultVO> groupByCategoryWorkOrderCount(String roleCode, String leadUnitId);

    Long signCategoryIsNull();

    List<NormalWorkOrderRankingResultVO> getRanking();
    IPage<ChiefWorkOrderItemResultVO> selectOrderAllConditions(IPage<ChiefWorkOrderItem> page, Wrapper<ChiefWorkOrderItem> wrapper, ChiefWorkOrderItemPageQuery model);
    List<ChiefWorkOrderItemResultVO> selectOrderAllConditions(Wrapper<ChiefWorkOrderItem> wrapper, ChiefWorkOrderItemPageQuery model);

}
