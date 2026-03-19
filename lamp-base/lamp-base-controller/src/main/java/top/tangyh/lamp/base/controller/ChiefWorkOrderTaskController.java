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
import top.tangyh.lamp.base.entity.ChiefWorkOrderTask;
import top.tangyh.lamp.base.service.ChiefWorkOrderTaskService;
import top.tangyh.lamp.base.vo.query.NormalWorkOrderTaskPageQuery;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderTaskResultVO;
import top.tangyh.lamp.base.vo.save.NormalWorkOrderTaskSaveVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskUpdateVO;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * 督办工单子表
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/chiefWorkOrderTask")
@Tag(name = "督办工单子表")
public class ChiefWorkOrderTaskController extends SuperController<ChiefWorkOrderTaskService, Long, ChiefWorkOrderTask
        , NormalWorkOrderTaskSaveVO, NormalWorkOrderTaskUpdateVO, NormalWorkOrderTaskPageQuery, NormalWorkOrderTaskResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    /**
     * 镇级：督办工单批量签收/单个签收
     */
    @Operation(summary = "镇级：督办工单批量签收/单个签收", description = "镇级：督办工单批量签收/单个签收")
    @PostMapping("/sign")
    @WebLog("镇级：督办工单批量签收/单个签收")
    public R signChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO signVO) {
        ArgumentAssert.notEmpty(signVO.getOrderNoList(), "请选择工单");
        return R.success(superService.batchSignChiefWorkOrder(signVO));
    }

    /**
     * 镇级：督办工单批量交办/单个交办
     */
    @Operation(summary = "镇级：督办工单批量交办/单个交办", description = "镇级：督办工单批量交办/单个交办")
    @PostMapping("/processing")
    @WebLog("镇级：督办工单批量交办/单个交办")
    public R processingChiefWorkOrder(@RequestBody List<NormalWorkOrderTaskActionVO> processingVOList) {
        ArgumentAssert.notEmpty(processingVOList, "请选择工单");
        return R.success(superService.batchProcessingChiefWorkOrder(processingVOList));
    }

    /**
     * 镇级：督办工单批量退回/单个退回
     */
    @Operation(summary = "镇级：督办工单批量退回/单个退回", description = "镇级：督办工单批量退回/单个退回")
    @PostMapping("/back")
    @WebLog("镇级：督办工单批量退回/单个退回")
    public R backChiefWorkOrder(@RequestBody List<NormalWorkOrderTaskActionVO> backVOList) {
        ArgumentAssert.notEmpty(backVOList, "请选择工单");
        return R.success(superService.batchBackChiefWorkOrder(backVOList));
    }

    /**
     * 镇级：督办工单结案
     */
    @Operation(summary = "镇级：督办工单结案", description = "镇级：督办工单结案")
    @PostMapping("/finish")
    @WebLog("镇级：督办工单结案")
    public R finishChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO finishVO) {
        ArgumentAssert.notNull(finishVO, "请选择工单");
        return R.success(superService.finishChiefWorkOrder(finishVO));
    }

    /**
     * 基层：督办工单签收
     */
    @Operation(summary = "基层：督办工单签收", description = "基层：督办工单签收")
    @PostMapping("/basic/sign")
    @WebLog("基层：督办工单签收")
    public R basicSignChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO signVO) {
        ArgumentAssert.notNull(signVO, "请选择工单");
        return R.success(superService.basicSignChiefWorkOrder(signVO));
    }

    /**
     * 基层：督办工单退回
     */
    @Operation(summary = "基层：督办工单退回", description = "基层：督办工单退回")
    @PostMapping("/basic/back")
    @WebLog("基层：督办工单退回")
    public R basicBackChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO backVO) {
        ArgumentAssert.notNull(backVO, "请选择工单");
        return R.success(superService.basicBackChiefWorkOrder(backVO));
    }

    /**
     * 基层：督办工单结案
     */
    @Operation(summary = "基层：督办工单结案", description = "基层：督办工单结案")
    @PostMapping("/basic/finish")
    @WebLog("基层：督办工单结案")
    public R basicFinishChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO finishVO) {
        ArgumentAssert.notNull(finishVO, "请选择工单");
        return R.success(superService.basicFinishChiefWorkOrder(finishVO));
    }

    /**
     * 基层负责人：督办工单审核
     */
    @Operation(summary = "基层负责人：督办工单审核", description = "基层负责人：督办工单审核")
    @PostMapping("/basic/directorAudit")
    @WebLog("基层负责人：督办工单审核")
    public R basicDirectorAuditChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO auditVO) {
        ArgumentAssert.notNull(auditVO, "请选择工单");
        return R.success(superService.basicDirectorAuditChiefWorkOrder(auditVO));
    }

    /**
     * 基层领导：督办工单审核
     */
    @Operation(summary = "基层领导：督办工单审核", description = "基层领导：督办工单审核")
    @PostMapping("/basic/leaderAudit")
    @WebLog("基层领导：督办工单审核")
    public R basicLeaderAuditChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO auditVO) {
        ArgumentAssert.notNull(auditVO, "请选择工单");
        return R.success(superService.basicLeaderAuditChiefWorkOrder(auditVO));
    }

    /**
     * 镇级：处理基层督办工单退回
     */
    @Operation(summary = "镇级：处理基层督办工单退回", description = "镇级：处理基层督办工单退回")
    @PostMapping("/townBasic/back")
    @WebLog("镇级：处理基层督办工单退回")
    public R townBasicBackChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO backVO) {
        ArgumentAssert.notNull(backVO, "请选择工单");
        return R.success(superService.townBasicBackChiefWorkOrder(backVO));
    }

    /**
     * 镇级：处理基层督办工单结案
     */
    @Operation(summary = "镇级：处理基层督办工单结案", description = "镇级：处理基层督办工单结案")
    @PostMapping("/townBasic/finish")
    @WebLog("镇级：处理基层督办工单结案")
    public R townBasicFinishChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO finishVO) {
        ArgumentAssert.notNull(finishVO, "请选择工单");
        return R.success(superService.townBasicFinishChiefWorkOrder(finishVO));
    }

    /**
     * 镇级：督办工单撤回
     */
    @Operation(summary = "镇级：督办工单撤回", description = "镇级：督办工单撤回")
    @PostMapping("/revoke")
    @WebLog("镇级：督办工单撤回")
    public R revokeChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO revokeVO) {
        ArgumentAssert.notNull(revokeVO, "请选择工单");
        return R.success(superService.revokeChiefWorkOrder(revokeVO));
    }

    /**
     * 镇级：督办工单催办
     */
    @Operation(summary = "镇级：督办工单催办", description = "镇级：督办工单催办")
    @PostMapping("/urge")
    @WebLog("镇级：督办工单催办")
    public R urgeChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO urgeVO) {
        ArgumentAssert.notNull(urgeVO, "请选择工单");
        return R.success(superService.urgeChiefWorkOrder(urgeVO));
    }

    /**
     * 镇级：督办工单再次交办
     */
    @Operation(summary = "镇级：督办工单再次交办", description = "镇级：督办工单再次交办")
    @PostMapping("/againProcessing")
    @WebLog("镇级：督办工单再次交办")
    public R againProcessingChiefWorkOrder(@RequestBody NormalWorkOrderTaskActionVO processingVO) {
        ArgumentAssert.notNull(processingVO, "请选择工单");
        return R.success(superService.againProcessingChiefWorkOrder(processingVO));
    }
}
