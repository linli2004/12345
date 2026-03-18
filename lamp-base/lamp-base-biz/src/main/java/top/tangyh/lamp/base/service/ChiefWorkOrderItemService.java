package top.tangyh.lamp.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.base.entity.ChiefWorkOrderItem;
import top.tangyh.lamp.base.vo.query.ChiefWorkOrderItemPageQuery;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderItemResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderRankingResultVO;
import top.tangyh.lamp.base.vo.result.SignCategoryIsNullNormalWorkOrderResultVO;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 业务接口
 * 督办工单详情
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
public interface ChiefWorkOrderItemService extends SuperService<Long, ChiefWorkOrderItem> {

    /**
     * 分页查询
     *
     * @param params 分页参数
     * @return 分页结果
     */
    IPage<ChiefWorkOrderItemResultVO> findPageResultVO(PageParams<ChiefWorkOrderItemPageQuery> params);

    /**
     * 列表查询
     *
     * @param model 查询条件
     * @return 列表结果
     */
    List<ChiefWorkOrderItemResultVO> selectListResultVO(ChiefWorkOrderItemPageQuery model);

    /**
     * 导出
     *
     * @param orderNoList 工单编号列表
     * @param response    响应
     * @param status      状态
     */
    void exportTaskZip(List<String> orderNoList, HttpServletResponse response, String status);

    Long getWorkOrderCount(String displayStatus, String roleCode, String leadUnitId);

    List<SignCategoryIsNullNormalWorkOrderResultVO> groupByCategoryWorkOrderCount(String roleCode, String leadUnitId);

    Long signCategoryIsNull();

    List<NormalWorkOrderRankingResultVO> getRanking();
    /**
     * 分页查询(带评论)
     *
     * @param params 分页参数
     * @return 分页结果
     */
    IPage<ChiefWorkOrderItemResultVO> findCommentedPageResultVO(PageParams<ChiefWorkOrderItemPageQuery> params);

    /**
     * 分页查询(未评论)
     *
     * @param params 分页参数
     * @return 分页结果
     */
    IPage<ChiefWorkOrderItemResultVO> findNotCommentPageResultVO(PageParams<ChiefWorkOrderItemPageQuery> params);
}
