package top.tangyh.lamp.base.manager;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.vo.query.NormalWorkOrderPageQuery;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderRankingResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderResultVO;
import top.tangyh.lamp.base.vo.result.SignCategoryIsNullNormalWorkOrderResultVO;

import java.util.List;

/**
 * <p>
 * 通用业务接口
 * 普通工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:47:40
 * @create [2026-03-03 11:47:40] [lunar] [代码生成器生成]
 */
public interface NormalWorkOrderManager extends SuperManager<NormalWorkOrder> {

    IPage<NormalWorkOrderResultVO> selectPageResultVO(IPage<NormalWorkOrder> page, Wrapper<NormalWorkOrder> wrapper, NormalWorkOrderPageQuery model);

    List<NormalWorkOrderResultVO> selectListResultVO(NormalWorkOrderPageQuery model);

    Long getWorkOrderCount(String displayStatus, String roleCode, String leadUnitId);

    List<SignCategoryIsNullNormalWorkOrderResultVO> groupByCategoryWorkOrderCount(String roleCode, String leadUnitId);

    Long signCategoryIsNull();

    List<NormalWorkOrderRankingResultVO> getRanking();

    IPage<NormalWorkOrderResultVO> selectNotCommentResultVO(IPage<NormalWorkOrder> page, Wrapper<NormalWorkOrder> wrapper, NormalWorkOrderPageQuery model);

    IPage<NormalWorkOrderResultVO> selectCommentedResultVO(IPage<NormalWorkOrder> page, Wrapper<NormalWorkOrder> wrapper, NormalWorkOrderPageQuery model);
}


