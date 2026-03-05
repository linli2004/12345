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
import top.tangyh.lamp.base.entity.WorkOrderDynamic;
import top.tangyh.lamp.base.service.WorkOrderDynamicService;
import top.tangyh.lamp.base.vo.query.WorkOrderDynamicPageQuery;
import top.tangyh.lamp.base.vo.result.WorkOrderDynamicResultVO;
import top.tangyh.lamp.base.vo.save.WorkOrderDynamicSaveVO;
import top.tangyh.lamp.base.vo.update.WorkOrderDynamicUpdateVO;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * 工单办理动态
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:48:11
 * @create [2026-03-03 11:48:11] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/workOrderDynamic")
@Tag(name = "工单办理动态")
public class WorkOrderDynamicController extends SuperController<WorkOrderDynamicService, Long, WorkOrderDynamic
        , WorkOrderDynamicSaveVO, WorkOrderDynamicUpdateVO, WorkOrderDynamicPageQuery, WorkOrderDynamicResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Override
    public R<List<WorkOrderDynamicResultVO>> query(WorkOrderDynamicPageQuery data) {
        WorkOrderDynamic entity = BeanPlusUtil.toBean(data, WorkOrderDynamic.class);
        QueryWrap<WorkOrderDynamic> wrapper = Wraps.q(entity).orderByAsc("created_time");
        List<WorkOrderDynamic> list = getSuperService().list(wrapper);
        return success(BeanPlusUtil.toBeanList(list, getResultVOClass()));
    }
}


