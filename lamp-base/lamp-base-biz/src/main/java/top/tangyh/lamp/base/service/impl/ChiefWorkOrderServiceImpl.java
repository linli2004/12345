package top.tangyh.lamp.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.dynamic.datasource.annotation.DS;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.Constant;
import top.tangyh.lamp.base.entity.*;
import top.tangyh.lamp.base.manager.ChiefWorkOrderItemManager;
import top.tangyh.lamp.base.manager.ChiefWorkOrderManager;
import top.tangyh.lamp.base.manager.NormalWorkOrderTaskManager;
import top.tangyh.lamp.base.manager.WorkOrderDynamicManager;
import top.tangyh.lamp.base.property.WorkExportFolderProperty;
import top.tangyh.lamp.base.service.ChiefWorkOrderService;
import top.tangyh.lamp.base.vo.update.ChiefWorkOrderTaskActionVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.file.service.FileService;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 业务实现类
 * 督办工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@Service
@DS(DsConstant.BASE_TENANT)
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChiefWorkOrderServiceImpl extends SuperServiceImpl<ChiefWorkOrderManager, Long, ChiefWorkOrder> implements ChiefWorkOrderService {

    private final ChiefWorkOrderItemManager chiefWorkOrderItemManager;
    private final WorkOrderDynamicManager workOrderDynamicManager;
    private final NormalWorkOrderTaskManager normalWorkOrderTaskManager;
    private final WorkExportFolderProperty workExportFolderProperty;
    private final FileService fileService;

    private final UidGenerator uidGenerator;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importChiefWorkOrder(InputStream inputStream, LocalDateTime supervisionTime, String name, ChiefWorkOrderTaskActionVO actionVO) {
        // 1. 生成批次号
        String batchNo = generateBatchNo(supervisionTime);

        // 2. 保存主表信息
        ChiefWorkOrder chiefWorkOrder = ChiefWorkOrder.builder()
                .batchNo(batchNo)
                .name(name)
                .status("未完成")
                .importTime(LocalDateTime.now())
                .build();
        superManager.save(chiefWorkOrder);

        // 3. 解析Excel并保存子表信息
        List<ChiefWorkOrderItem> itemList = Lists.newArrayList();
        List<NormalWorkOrderTask> normalWorkOrderTaskList = Lists.newArrayList();
        List<WorkOrderDynamic> workOrderDynamicList = Lists.newArrayList();
        EasyExcel.read(inputStream, ChiefWorkOrderItemExcel.class, new PageReadListener<ChiefWorkOrderItemExcel>(dataList -> {
            for (ChiefWorkOrderItemExcel excel : dataList) {
                ChiefWorkOrderItem item = BeanUtil.toBean(excel, ChiefWorkOrderItem.class);
                item.setBatchNo(batchNo);
                
                // 处理LocalDateTime字段
                item.setId(uidGenerator.getUid());
                item.setSupervisionReturnTime(parseDateTime(excel.getSupervisionReturnTime()));
                item.setSupervisionFinishTime(parseDateTime(excel.getSupervisionFinishTime()));
                item.setPlanFinishTime(parseDateTime(excel.getPlanFinishTime()));
                
                itemList.add(item);

                NormalWorkOrderTask taskTemp = new NormalWorkOrderTask();
                taskTemp.setId(uidGenerator.getUid());
                taskTemp.setOrderNo(item.getId().toString());
                taskTemp.setCurrentNodeCode(Constant.NODE_CODE_TOWN_SIGN);
                taskTemp.setLeadUnitId(1L);
                normalWorkOrderTaskList.add(taskTemp);
                //存入办理动态
                WorkOrderDynamic dynamicTemp = new WorkOrderDynamic();
                dynamicTemp.setOrderNo(item.getId().toString());
                dynamicTemp.setTaskId(taskTemp.getId());
                dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_SIGN);
                dynamicTemp.setOperatorId(actionVO.getOperatorId());
                dynamicTemp.setOperatorName(actionVO.getOperatorName());
                dynamicTemp.setOperatorPhone(actionVO.getOperatorPhone());
                dynamicTemp.setDeptId(actionVO.getDeptId());
                dynamicTemp.setDeptName(actionVO.getDeptName());
                dynamicTemp.setProcessType("导入");
                workOrderDynamicList.add(dynamicTemp);
            }
        })).headRowNumber(2).sheet().doRead();

        if (!itemList.isEmpty()) {
            chiefWorkOrderItemManager.saveBatch(itemList);
            workOrderDynamicManager.saveBatch(workOrderDynamicList);
            normalWorkOrderTaskManager.saveBatch(normalWorkOrderTaskList);
        }


    }

    @Override
    @SneakyThrows
    public void downloadTemplate(HttpServletResponse response) {
        ArgumentAssert.notBlank(workExportFolderProperty.getChiefWorkOrderTemplatePath(), "未配置督办工单模板路径");
        
        String fileName = URLEncoder.encode("督办工单导入模板.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        
        try (InputStream is = Files.newInputStream(Paths.get(workExportFolderProperty.getChiefWorkOrderTemplatePath()))) {
            is.transferTo(response.getOutputStream());
        } catch (Exception e) {
            log.error("下载督办工单模板失败", e);
            throw e;
        }
    }

    private String generateBatchNo(LocalDateTime supervisionTime) {
        String dateStr = supervisionTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 查询当天已有的最大流水号
        LbQueryWrap<ChiefWorkOrder> wrapper = Wraps.<ChiefWorkOrder>lbQ()
                .likeRight(ChiefWorkOrder::getBatchNo, dateStr)
                .orderByDesc(ChiefWorkOrder::getBatchNo)
                .last("limit 1");
        
        ChiefWorkOrder lastOrder = superManager.getOne(wrapper);
        int sequence = 1;
        if (lastOrder != null) {
            String lastBatchNo = lastOrder.getBatchNo();
            if (StrUtil.isNotBlank(lastBatchNo) && lastBatchNo.length() >= 14) {
                try {
                    String lastSeqStr = lastBatchNo.substring(lastBatchNo.length() - 3);
                    sequence = Integer.parseInt(lastSeqStr) + 1;
                } catch (NumberFormatException e) {
                    log.warn("解析流水号失败: {}", lastBatchNo);
                }
            }
        }
        
        return dateStr + "-" + String.format("%03d", sequence);
    }

    private LocalDateTime parseDateTime(String dateStr) {
        if (StrUtil.isBlank(dateStr)) {
            return null;
        }
        try {
            // 尝试多种格式解析，根据Excel实际情况调整
            if (dateStr.contains("-")) {
                if (dateStr.length() <= 10) {
                    return LocalDateTime.parse(dateStr + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else if (dateStr.contains("/")) {
                if (dateStr.length() <= 10) {
                    return LocalDateTime.parse(dateStr + " 00:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
                }
                return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
            }
            // 简单处理，如果解析失败返回null或抛出异常，根据业务需求定
            return null;
        } catch (Exception e) {
            log.warn("日期解析失败: {}", dateStr);
            return null;
        }
    }
}
