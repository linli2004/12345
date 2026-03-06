package top.tangyh.lamp.base.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.Constant;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.entity.NormalWorkOrderTask;
import top.tangyh.lamp.base.service.NormalWorkOrderService;
import top.tangyh.lamp.base.service.NormalWorkOrderTaskService;
import top.tangyh.lamp.base.vo.query.NormalWorkOrderPageQuery;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderResultVO;
import top.tangyh.lamp.base.vo.save.NormalWorkOrderSaveVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderUpdateVO;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 前端控制器
 * 普通工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:47:40
 * @create [2026-03-03 11:47:40] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/normalWorkOrder")
@Tag(name = "普通工单")
public class NormalWorkOrderController extends SuperController<NormalWorkOrderService, Long, NormalWorkOrder
        , NormalWorkOrderSaveVO, NormalWorkOrderUpdateVO, NormalWorkOrderPageQuery, NormalWorkOrderResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    private final NormalWorkOrderTaskService normalWorkOrderTaskService;

    /**
     * 普通工单导入
     *
     * @param actionVO excel工单文件
     */
    @Operation(summary = "普通工单导入", description = "普通工单导入")
    @PostMapping("/import")
    @WebLog("普通工单导入")
    public R importNormalWorkOrder(NormalWorkOrderTaskActionVO actionVO) throws IOException {
        if (actionVO.getFile().isEmpty()) {
            return R.fail("导入的文件为空");
        }
        List<String> errorOrderNoList = Lists.newArrayList();
        try (InputStream inputStream = actionVO.getFile().getInputStream()) {
            superService.importNormalWorkOrder(inputStream, errorOrderNoList, actionVO);
        }
        if (CollectionUtils.isEmpty(errorOrderNoList)) {
            return R.success();
        } else {
            return R.success(errorOrderNoList, "部分工单导入失败");
        }
    }

    /**
     * 普通工单查询（按业务节点编码和角色编码查询）
     */
    @Override
    @WebLog(value = "'分页列表查询:第' + #params?.current + '页, 显示' + #params?.size + '行'", response = false)
    public R<IPage<NormalWorkOrderResultVO>> page(@RequestBody @Validated PageParams<NormalWorkOrderPageQuery> params) {
        IPage<NormalWorkOrderResultVO> page = superService.findPageResultVO(params);
        List<String> orderNoList = page.getRecords().stream().map(NormalWorkOrderResultVO::getOrderNo).toList();
        List<NormalWorkOrderTask> workOrderTaskList = null;
        if (Constant.ROLE_CODE_TOWN_SPECIALIST.equals(params.getModel().getRoleCode())) {
            workOrderTaskList = normalWorkOrderTaskService.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).in(NormalWorkOrderTask::getOrderNo, orderNoList));
        } else {
            workOrderTaskList = normalWorkOrderTaskService.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getLeadUnitId, params.getModel().getLeadUnitId()).eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).in(NormalWorkOrderTask::getOrderNo, orderNoList));
        }
        Map<String, List<NormalWorkOrderTask>> taskMap = workOrderTaskList.stream()
                .collect(Collectors.groupingBy(
                        NormalWorkOrderTask::getOrderNo,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        page.getRecords().forEach(t -> t.setWorkOrderTaskList(taskMap.get(t.getOrderNo())));
        return R.success(page);
    }


    @PostMapping("/export-complex-zip")
    public void exportComplexZip(@RequestBody List<Long> idList, HttpServletResponse response) throws IOException {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"project_package.zip\"");
        List<NormalWorkOrder> normalWorkOrders = superService.listByIds(idList);
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {

            ZipEntry excelEntry = new ZipEntry("工单.xlsx");
            zos.putNextEntry(excelEntry);
            zos.write(new byte[]{/* Excel 字节数据 */});
            zos.closeEntry();

            for (NormalWorkOrder normalWorkOrder : normalWorkOrders) {
                String folderName = normalWorkOrder.getOrderNo() + "/";
                ZipEntry directoryEntry = new ZipEntry(folderName);
                zos.putNextEntry(directoryEntry);
                zos.closeEntry();

                ZipEntry wordEntry = new ZipEntry(folderName + "test.docx");
                zos.putNextEntry(wordEntry);
                zos.write(new byte[]{});
                zos.closeEntry();
            }

            zos.finish();
        }
    }
}


