package top.tangyh.lamp.base.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.base.entity.ChiefWorkOrder;
import top.tangyh.lamp.base.service.ChiefWorkOrderService;
import top.tangyh.lamp.base.vo.query.ChiefWorkOrderPageQuery;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderResultVO;
import top.tangyh.lamp.base.vo.save.ChiefWorkOrderSaveVO;
import top.tangyh.lamp.base.vo.update.ChiefWorkOrderTaskActionVO;
import top.tangyh.lamp.base.vo.update.ChiefWorkOrderUpdateVO;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * 督办工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/chiefWorkOrder")
@RequiredArgsConstructor
@Tag(name = "督办工单")
public class ChiefWorkOrderController extends SuperController<ChiefWorkOrderService, Long, ChiefWorkOrder, ChiefWorkOrderSaveVO, ChiefWorkOrderUpdateVO, ChiefWorkOrderPageQuery, ChiefWorkOrderResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @SneakyThrows
    @Operation(summary = "导入督办工单", description = "导入督办工单")
    @PostMapping(path = "/import", consumes = "multipart/form-data")
    public R<Boolean> importChiefWorkOrder(@RequestPart("file") MultipartFile file,
                                           @RequestParam("supervisionTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime supervisionTime,
                                           @RequestParam("name") String name,
                                           ChiefWorkOrderTaskActionVO actionVO) {
        superService.importChiefWorkOrder(file.getInputStream(), supervisionTime, name, actionVO);
        return R.success(true);
    }

    @Operation(summary = "下载督办工单模板", description = "下载督办工单模板")
    @PostMapping(path = "/downloadTemplate", produces = "application/octet-stream")
    public void downloadTemplate(HttpServletResponse response) {
        superService.downloadTemplate(response);
    }

    @Operation(summary = "生成批次编号", description = "生成批次编号")
    @GetMapping(path = "/generateBatchNo")
    public R<String> generateBatchNo(LocalDateTime supervisionTime) {
        return R.success(superService.generateBatchNo(supervisionTime));
    }

    @PostMapping("/export")
    public void export(@RequestParam String batchNo, HttpServletResponse response) {
        superService.exportTaskZip(batchNo, response);
    }
}
