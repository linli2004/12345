package top.tangyh.lamp.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.tangyh.basic.base.mapper.SuperMapper;
import top.tangyh.lamp.base.entity.ChiefWorkOrderItem;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.vo.query.ChiefWorkOrderItemPageQuery;
import top.tangyh.lamp.base.vo.query.NormalWorkOrderPageQuery;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderItemResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderRankingResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderResultVO;
import top.tangyh.lamp.base.vo.result.SignCategoryIsNullNormalWorkOrderResultVO;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * 督办工单详情
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Repository
public interface ChiefWorkOrderItemMapper extends SuperMapper<ChiefWorkOrderItem> {

    /**
     * 分页查询
     *
     * @param page  分页参数
     * @param query 查询条件
     * @param model 查询模型
     * @return 分页结果
     */
    IPage<ChiefWorkOrderItemResultVO> selectPageResultVO(IPage<ChiefWorkOrderItem> page, @Param(Constants.WRAPPER) Wrapper<ChiefWorkOrderItem> query, @Param("model") ChiefWorkOrderItemPageQuery model);

    /**
     * 列表查询
     *
     * @param model 查询模型
     * @return 列表结果
     */
    List<ChiefWorkOrderItemResultVO> selectListResultVO(@Param("model") ChiefWorkOrderItemPageQuery model);
    Integer selectCountResultVO(@Param("model") ChiefWorkOrderItemPageQuery model);

    /**
     * 分页查询(带评论)
     *
     * @param page  分页参数
     * @param query 查询条件
     * @param model 查询模型
     * @return 分页结果
     */
    IPage<ChiefWorkOrderItemResultVO> selectCommentedResultVO(IPage<ChiefWorkOrderItem> page, @Param(Constants.WRAPPER) Wrapper<ChiefWorkOrderItem> query, @Param("model") ChiefWorkOrderItemPageQuery model);

    /**
     * 分页查询(未评论)
     *
     * @param page  分页参数
     * @param query 查询条件
     * @param model 查询模型
     * @return 分页结果
     */
    IPage<ChiefWorkOrderItemResultVO> selectNotCommentResultVO(IPage<ChiefWorkOrderItem> page, @Param(Constants.WRAPPER) Wrapper<ChiefWorkOrderItem> query, @Param("model") ChiefWorkOrderItemPageQuery model);

    Long getWorkOrderCount(@Param("displayStatus") String displayStatus, @Param("roleCode") String roleCode, @Param("leadUnitId") String leadUnitId);

    List<SignCategoryIsNullNormalWorkOrderResultVO> groupByCategoryWorkOrderCount(@Param("roleCode") String roleCode, @Param("leadUnitId") String leadUnitId);

    Long signCategoryIsNull();

    List<NormalWorkOrderRankingResultVO> getRanking();

    IPage<ChiefWorkOrderItemResultVO> selectOrderAllConditions(IPage<ChiefWorkOrderItem> page,
                                                            @Param(Constants.WRAPPER) Wrapper<ChiefWorkOrderItem> wrapper,
                                                            @Param("model") ChiefWorkOrderItemPageQuery model);

}
