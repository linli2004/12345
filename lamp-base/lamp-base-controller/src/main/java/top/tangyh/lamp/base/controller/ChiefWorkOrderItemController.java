package top.tangyh.lamp.base.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.base.entity.ChiefWorkOrderItem;
import top.tangyh.lamp.base.service.ChiefWorkOrderItemService;

import top.tangyh.basic.base.R;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.lamp.Constant;
import top.tangyh.lamp.base.entity.ChiefWorkOrderTask;
import top.tangyh.lamp.base.service.ChiefWorkOrderTaskService;
import top.tangyh.lamp.base.vo.query.ChiefWorkOrderItemPageQuery;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderItemResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderTaskResultVO;
import top.tangyh.lamp.base.vo.save.ChiefWorkOrderItemSaveVO;
import top.tangyh.lamp.base.vo.update.ChiefWorkOrderItemUpdateVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 前端控制器
 * 督办工单详情
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/chiefWorkOrderItem")
@Tag(name = "督办工单详情")
public class ChiefWorkOrderItemController extends SuperController<ChiefWorkOrderItemService, Long, ChiefWorkOrderItem
        , ChiefWorkOrderItemSaveVO, ChiefWorkOrderItemUpdateVO, ChiefWorkOrderItemPageQuery, ChiefWorkOrderItemResultVO> {
    private final EchoService echoService;
    private final ChiefWorkOrderItemService chiefWorkOrderItemService;
    private final ChiefWorkOrderTaskService chiefWorkOrderTaskService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Operation(summary = "查询统计 督办工单", description = "查询统计 督办工单")
    @PostMapping(path = "/pageChiefStatistic")
    @WebLog("查询统计 督办工单")
    public R<IPage<ChiefWorkOrderItemResultVO>> pageStatistic(@RequestBody @Validated PageParams<ChiefWorkOrderItemPageQuery> params) {
        IPage<ChiefWorkOrderItemResultVO> page = superService.selectOrderAllConditions(params);
        List<String> orderNoList = page.getRecords().stream().filter(item -> "办结".equals(item.getStatus())).map(ChiefWorkOrderItemResultVO::getWorkOrderNo).toList();
        List<ChiefWorkOrderTask> workOrderTaskList = chiefWorkOrderTaskService.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).in(ChiefWorkOrderTask::getOrderNo, orderNoList));
        if (CollectionUtils.isEmpty(workOrderTaskList)) return R.success(page);
        List<NormalWorkOrderTaskResultVO> taskResultVOList = BeanUtil.copyToList(workOrderTaskList, NormalWorkOrderTaskResultVO.class);
        if (echoService != null) {
            echoService.action(taskResultVOList);
        }
        Map<String, List<NormalWorkOrderTaskResultVO>> taskMap = taskResultVOList.stream()
                .collect(Collectors.groupingBy(
                        NormalWorkOrderTaskResultVO::getOrderNo,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        page.getRecords().forEach(t -> t.setWorkOrderTaskList(taskMap.get(t.getWorkOrderNo())));
        
        params.getModel().setOrderNoList(orderNoList);
        params.getModel().setDisplayStatus("办结");

        if (!CollectionUtils.isEmpty(orderNoList)) {
            superService.getFinishOrBackContentJson(page.getRecords(), params.getModel());
        }

        return R.success(page);
    }

    @Override
    public R<IPage<ChiefWorkOrderItemResultVO>> page(@RequestBody @Validated PageParams<ChiefWorkOrderItemPageQuery> params) {
        IPage<ChiefWorkOrderItemResultVO> pageResultVO = superService.findPageResultVO(params);
        return R.success(pageResultVO);
    }

    @PostMapping("/list")
    public R<List<ChiefWorkOrderItemResultVO>> list(@RequestBody ChiefWorkOrderItemPageQuery model) {
        return R.success(superService.selectListResultVO(model));
    }

    @PostMapping("/export")
    public void export(@RequestBody List<String> orderNoList, HttpServletResponse response, String status) {
        superService.exportTaskZip(orderNoList, response, status);
    }

    @Operation(summary = "督办工单待批示", description = "督办工单待批示")
    @PostMapping(path = "/notComment")
    @WebLog("督办工单待批示")
    public R<IPage<ChiefWorkOrderItemResultVO>> notCommentPage(@RequestBody @Validated PageParams<ChiefWorkOrderItemPageQuery> params) {
        return R.success(superService.findNotCommentPageResultVO(params));
    }

    @Operation(summary = "督办工单已批示", description = "督办工单已批示")
    @PostMapping(path = "/commented")
    @WebLog("督办工单已批示")
    public R<IPage<ChiefWorkOrderItemResultVO>> commentedPage(@RequestBody @Validated PageParams<ChiefWorkOrderItemPageQuery> params) {
        return R.success(superService.findCommentedPageResultVO(params));
    }
}
