package top.tangyh.lamp.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.tangyh.basic.base.mapper.SuperMapper;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.vo.query.NormalWorkOrderPageQuery;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderRankingResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderResultVO;
import top.tangyh.lamp.base.vo.result.SignCategoryIsNullNormalWorkOrderResultVO;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * 普通工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:47:40
 * @create [2026-03-03 11:47:40] [lunar] [代码生成器生成]
 */
@Repository
public interface NormalWorkOrderMapper extends SuperMapper<NormalWorkOrder> {

    IPage<NormalWorkOrderResultVO> selectPageResultVO(IPage<NormalWorkOrder> page,
                                                      @Param(Constants.WRAPPER) Wrapper<NormalWorkOrder> wrapper,
                                                      @Param("model") NormalWorkOrderPageQuery model);

    List<NormalWorkOrderResultVO> selectListResultVO(@Param("model") NormalWorkOrderPageQuery model);

    Long getWorkOrderCount(@Param("displayStatus") String displayStatus, @Param("roleCode") String roleCode, @Param("leadUnitId") String leadUnitId, @Param("leadEmployeeId") String leadEmployeeId);

    List<SignCategoryIsNullNormalWorkOrderResultVO> groupByCategoryWorkOrderCount(@Param("roleCode") String roleCode, @Param("leadUnitId") String leadUnitId, @Param("leadEmployeeId") String leadEmployeeId);

    Long signCategoryIsNull();

    List<NormalWorkOrderRankingResultVO> getRanking();

    IPage<NormalWorkOrderResultVO> selectNotCommentResultVO(IPage<NormalWorkOrder> page,
                                                            @Param(Constants.WRAPPER) Wrapper<NormalWorkOrder> wrapper,
                                                            @Param("model") NormalWorkOrderPageQuery model);

    IPage<NormalWorkOrderResultVO> selectCommentedPageResultVO(IPage<NormalWorkOrder> page,
                                                               @Param(Constants.WRAPPER) Wrapper<NormalWorkOrder> wrapper,
                                                               @Param("model") NormalWorkOrderPageQuery model);

    IPage<NormalWorkOrderResultVO> selectOrderAllConditions(IPage<NormalWorkOrder> page,
                                                               @Param(Constants.WRAPPER) Wrapper<NormalWorkOrder> wrapper,
                                                               @Param("model") NormalWorkOrderPageQuery model);
}


