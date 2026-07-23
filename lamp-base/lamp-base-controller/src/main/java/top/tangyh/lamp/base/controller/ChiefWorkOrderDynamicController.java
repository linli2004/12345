package top.tangyh.lamp.base.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.QueryWrap;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.basic.utils.BeanPlusUtil;
import top.tangyh.lamp.base.entity.ChiefWorkOrderDynamic;
import top.tangyh.lamp.base.service.ChiefWorkOrderDynamicService;
import top.tangyh.lamp.base.vo.query.WorkOrderDynamicPageQuery;
import top.tangyh.lamp.base.vo.result.WorkOrderDynamicResultVO;
import top.tangyh.lamp.base.vo.save.WorkOrderDynamicSaveVO;
import top.tangyh.lamp.base.vo.update.WorkOrderDynamicUpdateVO;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * 督办工单办理动态
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/chiefWorkOrderDynamic")
@Tag(name = "督办工单办理动态")
public class ChiefWorkOrderDynamicController extends SuperController<ChiefWorkOrderDynamicService, Long, ChiefWorkOrderDynamic
        , WorkOrderDynamicSaveVO, WorkOrderDynamicUpdateVO, WorkOrderDynamicPageQuery, WorkOrderDynamicResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Override
    public R<List<WorkOrderDynamicResultVO>> query(WorkOrderDynamicPageQuery data) {
        ChiefWorkOrderDynamic entity = BeanPlusUtil.toBean(data, ChiefWorkOrderDynamic.class);
        QueryWrap<ChiefWorkOrderDynamic> wrapper = Wraps.q(entity).orderByAsc("created_time");
        List<ChiefWorkOrderDynamic> list = getSuperService().list(wrapper);
        return success(BeanPlusUtil.toBeanList(list, getResultVOClass()));
    }
}
