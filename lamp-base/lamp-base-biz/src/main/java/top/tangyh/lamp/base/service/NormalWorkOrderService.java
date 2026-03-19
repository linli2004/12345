package top.tangyh.lamp.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.http.HttpServletResponse;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.vo.query.NormalWorkOrderPageQuery;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderRankingResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderResultVO;
import top.tangyh.lamp.base.vo.result.SignCategoryIsNullNormalWorkOrderResultVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;

import java.io.InputStream;
import java.util.List;


/**
 * <p>
 * 业务接口
 * 普通工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:47:40
 * @create [2026-03-03 11:47:40] [lunar] [代码生成器生成]
 */
public interface NormalWorkOrderService extends SuperService<Long, NormalWorkOrder> {

    void importNormalWorkOrder(InputStream inputStream, List<String> errorOrderNoList, NormalWorkOrderTaskActionVO actionVO);

    IPage<NormalWorkOrderResultVO> findPageResultVO(PageParams<NormalWorkOrderPageQuery> params);
    void exportTaskZip(List<String> orderNoList, HttpServletResponse response, String status);

    List<NormalWorkOrderResultVO> selectListResultVO(NormalWorkOrderPageQuery model);

    void getFinishOrBackContentJson(List<NormalWorkOrderResultVO> resultVOList, NormalWorkOrderPageQuery model);

    Long getWorkOrderCount(String displayStatus, String roleCode, String leadUnitId);

    List<SignCategoryIsNullNormalWorkOrderResultVO> groupByCategoryWorkOrderCount(String roleCode, String leadUnitId);

    Long signCategoryIsNull();

    List<NormalWorkOrderRankingResultVO> getRanking();

    IPage<NormalWorkOrderResultVO> findNotCommentPageResultVO(PageParams<NormalWorkOrderPageQuery> params);

    IPage<NormalWorkOrderResultVO> findCommentedPageResultVO(PageParams<NormalWorkOrderPageQuery> params);

    IPage<NormalWorkOrderResultVO> selectOrderAllConditions(PageParams<NormalWorkOrderPageQuery> params);

}


