package top.tangyh.lamp.base.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.base.entity.NormalWorkOrderTask;
import top.tangyh.lamp.base.service.NormalWorkOrderTaskService;
import top.tangyh.lamp.base.vo.query.NormalWorkOrderTaskPageQuery;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderTaskResultVO;
import top.tangyh.lamp.base.vo.save.NormalWorkOrderTaskSaveVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskUpdateVO;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * 工单子表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:45:59
 * @create [2026-03-03 11:45:59] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/normalWorkOrderTask")
@Tag(name = "工单子表")
public class NormalWorkOrderTaskController extends SuperController<NormalWorkOrderTaskService, Long, NormalWorkOrderTask
        , NormalWorkOrderTaskSaveVO, NormalWorkOrderTaskUpdateVO, NormalWorkOrderTaskPageQuery, NormalWorkOrderTaskResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }


    /**
     * 镇级：普通工单批量签收/单个签收
     */
    @Operation(summary = "镇级：普通工单批量签收/单个签收", description = "镇级：普通工单批量签收/单个签收")
    @PostMapping("/sign")
    @WebLog("镇级：普通工单批量签收/单个签收")
    public R signNormalWorkOrder(@RequestBody NormalWorkOrderTaskActionVO signVO) {
        ArgumentAssert.notEmpty(signVO.getOrderNoList(), "请选择工单");
        return R.success(superService.batchSignNormalWorkOrder(signVO));
    }

    /**
     * 镇级：普通工单批量交办/单个交办
     */
    @Operation(summary = "镇级：普通工单批量交办/单个交办", description = "镇级：普通工单批量交办/单个交办")
    @PostMapping("/processing")
    @WebLog("镇级：普通工单批量交办/单个交办")
    public R processingNormalWorkOrder(@RequestBody List<NormalWorkOrderTaskActionVO> processingVOList) {
        ArgumentAssert.notEmpty(processingVOList, "请选择工单");
        return R.success(superService.batchProcessingNormalWorkOrder(processingVOList));
    }

    /**
     * 镇级：普通工单批量退回/单个退回
     */
    @Operation(summary = "镇级：普通工单批量退回/单个退回", description = "镇级：普通工单批量退回/单个退回")
    @PostMapping("/back")
    @WebLog("镇级：普通工单批量退回/单个退回")
    public R backNormalWorkOrder(@RequestBody List<NormalWorkOrderTaskActionVO> backVOList) {
        ArgumentAssert.notEmpty(backVOList, "请选择工单");
        return R.success(superService.batchBackNormalWorkOrder(backVOList));
    }

    /**
     * 镇级：普通工单结案
     */
    @Operation(summary = "镇级：普通工单结案", description = "镇级：普通工单结案")
    @PostMapping("/finish")
    @WebLog("镇级：普通工单结案")
    public R finishNormalWorkOrder(@RequestBody NormalWorkOrderTaskActionVO finishVO) {
        ArgumentAssert.notNull(finishVO, "请选择工单");
        return R.success(superService.finishNormalWorkOrder(finishVO));
    }

    /**
     * 基层：普通工单签收
     */
    @Operation(summary = "基层：普通工单签收", description = "基层：普通工单签收")
    @PostMapping("/basic/sign")
    @WebLog("基层：普通工单签收")
    public R basicSignNormalWorkOrder(@RequestBody NormalWorkOrderTaskActionVO signVO) {
        ArgumentAssert.notNull(signVO, "请选择工单");
        return R.success(superService.basicSignNormalWorkOrder(signVO));
    }

    /**
     * 基层：普通工单退回
     */
    @Operation(summary = "基层：普通工单退回", description = "基层：普通工单退回")
    @PostMapping("/basic/back")
    @WebLog("基层：普通工单退回")
    public R basicBackNormalWorkOrder(@RequestBody NormalWorkOrderTaskActionVO backVO) {
        ArgumentAssert.notNull(backVO, "请选择工单");
        return R.success(superService.basicBackNormalWorkOrder(backVO));
    }

    /**
     * 基层：普通工单结案
     */
    @Operation(summary = "基层：普通工单结案", description = "基层：普通工单结案")
    @PostMapping("/basic/finish")
    @WebLog("基层：普通工单结案")
    public R basicFinishNormalWorkOrder(@RequestBody NormalWorkOrderTaskActionVO finishVO) {
        ArgumentAssert.notNull(finishVO, "请选择工单");
        return R.success(superService.basicFinishNormalWorkOrder(finishVO));
    }
}


