package top.tangyh.lamp.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.dynamic.datasource.annotation.DS;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
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
import top.tangyh.lamp.base.service.ChiefWorkOrderItemService;
import top.tangyh.lamp.base.service.ChiefWorkOrderService;
import top.tangyh.lamp.base.vo.query.ChiefWorkOrderItemPageQuery;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderExport;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderItemResultVO;
import top.tangyh.lamp.base.vo.update.ChiefWorkOrderTaskActionVO;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.file.service.FileService;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
public class ChiefWorkOrderServiceImpl extends SuperServiceImpl<ChiefWorkOrderManager, Long, ChiefWorkOrder> implements ChiefWorkOrderService {

    private final ChiefWorkOrderItemManager chiefWorkOrderItemManager;
    private final ChiefWorkOrderItemService chiefWorkOrderItemService;
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

    @Override
    public String generateBatchNo(LocalDateTime supervisionTime) {
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
            return null;
        } catch (Exception e) {
            log.warn("日期解析失败: {}", dateStr);
            return null;
        }
    }

    @SneakyThrows
    @Override
    public void exportTaskZip(String batchNo, HttpServletResponse response) {
        // 1. 查询数据
        ChiefWorkOrderItemPageQuery chiefWorkOrderPageQuery = new ChiefWorkOrderItemPageQuery();
        chiefWorkOrderPageQuery.setBatchNo(batchNo);

        List<ChiefWorkOrderItemResultVO> chiefWorkOrderResultVOS = chiefWorkOrderItemService.selectOrderAllConditionsList(chiefWorkOrderPageQuery);
        List<String> finishOrderNoList = chiefWorkOrderResultVOS.stream().filter(item -> Objects.equals("办结", item.getStatus())).map(item -> String.valueOf(item.getId())).toList();
        chiefWorkOrderItemService.setContentJson(chiefWorkOrderResultVOS, "办结",finishOrderNoList);
        List<String> backOrderNoList = chiefWorkOrderResultVOS.stream().filter(item -> Objects.equals("退回", item.getStatus())).map(item -> String.valueOf(item.getId())).toList();
        chiefWorkOrderItemService.setContentJson(chiefWorkOrderResultVOS, "已退回",backOrderNoList);

        List<ChiefWorkOrderExport> chiefWorkOrderExports = BeanUtil.copyToList(chiefWorkOrderResultVOS, ChiefWorkOrderExport.class);
        // 2. 准备文件名和填充数据
        ArgumentAssert.notNull(workExportFolderProperty.getChiefWorkFinishExcelPath(), "未配置 Excel 模板路径");
        Path execlPath = Paths.get(workExportFolderProperty.getChiefWorkFinishExcelPath());
        String fileNamePrefix = "督办工单导出";

        String fileName = URLEncoder.encode(fileNamePrefix + ".zip", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);


        // 4. 生成ZIP
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
             InputStream excelTemplateInputStream = Files.newInputStream(execlPath)) {

            // 4.1 创建根目录
            String rootFolderName = fileNamePrefix + "/";
            zos.putNextEntry(new ZipEntry(rootFolderName));
            zos.closeEntry();

            // 4.2 生成Excel汇总表
            String exportTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            zos.putNextEntry(new ZipEntry(rootFolderName + "12345事件工作表(街镇)" + exportTime + ".xlsx"));
            ExcelWriter excelWriter = EasyExcel.write(zos)
                    .autoCloseStream(Boolean.FALSE)
                    .withTemplate(excelTemplateInputStream)
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet("12345事件工作表(街镇)").build();
            excelWriter.fill(chiefWorkOrderExports, writeSheet);
            excelWriter.finish();
            zos.closeEntry();

            // 4.3 生成每个工单的Word文档
            for (int i = 0; i < chiefWorkOrderExports.size(); i++) {
                int count = 1;
                int row = i + 1;
                ChiefWorkOrderExport chiefWorkOrderExport = chiefWorkOrderExports.get(i);
                List<Long> fileIds = extractFileIds(chiefWorkOrderExport);
                if (!CollectionUtils.isEmpty(fileIds)) {
                    Map<Long, String> urlMap = fileService.findUrlById(fileIds);
                    for (Long fid : fileIds) {
                        String fUrl = urlMap.get(fid);
                        if (StringUtils.isBlank(fUrl)) {
                            continue;
                        }
                        String attachmentsFileName = null;
                        if (chiefWorkOrderExport.getFinishOrBackDynamic() != null && StringUtils.isNotBlank(chiefWorkOrderExport.getFinishOrBackDynamic().getContentJson())) {
                            try {
                                JSONObject obj = JSON.parseObject(chiefWorkOrderExport.getFinishOrBackDynamic().getContentJson());
                                attachmentsFileName = obj.getString("situationDescFileName");
                            } catch (Exception e) {
                                log.error("文件下载失败: {}",fid);
                            }
                        }
                        String entryName = rootFolderName  + row + "-" +count + "-" + deriveFileName(fid, fUrl, attachmentsFileName);
                        count++;
                        zos.putNextEntry(new ZipEntry(entryName));
                        try (BufferedInputStream bis = new BufferedInputStream(new URL(fUrl).openStream())) {
                            bis.transferTo(zos);
                        } catch (Exception e) {
                            log.warn("下载附件失败, id={}, url={}", fid, fUrl, e);
                        }
                        zos.closeEntry();
                    }
                }
            }
            zos.finish();
        }
    }

    private List<Long> extractFileIds(ChiefWorkOrderExport export) {
        List<Long> fileIds = Lists.newArrayList();
        if (export.getFinishOrBackDynamic() != null && StringUtils.isNotBlank(export.getFinishOrBackDynamic().getContentJson())) {
            try {
                JSONObject jsonObject = JSON.parseObject(export.getFinishOrBackDynamic().getContentJson());
                // 尝试获取 attachments 字段
                String attachments = jsonObject.getString("situationDesc");
                if (StringUtils.isNotBlank(attachments)) {
                    fileIds.add(Long.parseLong(attachments));
                }
            } catch (Exception e) {
                log.warn("解析附件ID失败 id={}", export.getId(), e);
            }
        }
        return fileIds.stream().distinct().collect(Collectors.toList());
    }

    private String deriveFileName(Long fileId, String url, String jsonFileName) {
        if (StringUtils.isNotBlank(jsonFileName)) {
            return jsonFileName;
        }
        String fileName = String.valueOf(fileId);
        if (StringUtils.isNotBlank(url)) {
            try {
                String path = new java.net.URL(url).getPath();
                String tempName = path.substring(path.lastIndexOf('/') + 1);
                if (StringUtils.isNotBlank(tempName)) {
                    fileName = java.net.URLDecoder.decode(tempName, StandardCharsets.UTF_8);
                }
            } catch (Exception e) {
                log.error("解析文件名失败:{}", fileName, e);
            }
        }
        return fileName;
    }
}
