package top.tangyh.lamp.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.base.entity.ChiefWorkOrderDynamic;
import top.tangyh.lamp.base.entity.ChiefWorkOrderItem;
import top.tangyh.lamp.base.manager.ChiefWorkOrderDynamicManager;
import top.tangyh.lamp.base.manager.ChiefWorkOrderItemManager;
import top.tangyh.lamp.base.property.WorkExportFolderProperty;
import top.tangyh.lamp.base.service.ChiefWorkOrderItemService;
import top.tangyh.lamp.base.vo.query.ChiefWorkOrderItemPageQuery;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderExport;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderItemResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderRankingResultVO;
import top.tangyh.lamp.base.vo.result.SignCategoryIsNullNormalWorkOrderResultVO;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 业务实现类
 * 督办工单详情
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@Service
@DS(DsConstant.BASE_TENANT)
@RequiredArgsConstructor
public class ChiefWorkOrderItemServiceImpl extends SuperServiceImpl<ChiefWorkOrderItemManager, Long, ChiefWorkOrderItem> implements ChiefWorkOrderItemService {
    private final ChiefWorkOrderDynamicManager chiefWorkOrderDynamicManager;
    private final WorkExportFolderProperty workExportFolderProperty;
    private final FileService fileService;

    @Override
    public IPage<ChiefWorkOrderItemResultVO> findPageResultVO(PageParams<ChiefWorkOrderItemPageQuery> params) {
        IPage<ChiefWorkOrderItem> page = params.buildPage(ChiefWorkOrderItem.class);
        ChiefWorkOrderItemPageQuery model = params.getModel();
        Map<String, Object> extra = params.getExtra();
        LbQueryWrap<ChiefWorkOrderItem> wrap = Wraps.lbq(null, extra, ChiefWorkOrderItem.class);
        wrap.like(ChiefWorkOrderItem::getWorkOrderNo, model.getWorkOrderNo())
                .like(ChiefWorkOrderItem::getTitle, model.getTitle())
                .like(ChiefWorkOrderItem::getAppealContent, model.getAppealContent())
                .like(ChiefWorkOrderItem::getAppealType, model.getAppealType())
                .like(ChiefWorkOrderItem::getContactPhone, model.getContactPhone())
                .like(ChiefWorkOrderItem::getSettleCondition, model.getSettleCondition())
                .like(ChiefWorkOrderItem::getBatchNo, model.getBatchNo())
                .and(model.getIsJointQuery(), w -> {
                    if (model.getIsJointQuery() && StringUtils.isNotBlank(model.getKeyword())) {
                        w.like(ChiefWorkOrderItem::getWorkOrderNo, model.getKeyword())
                                .or()
                                .like(ChiefWorkOrderItem::getTitle, model.getKeyword())
                                .or()
                                .like(ChiefWorkOrderItem::getAppealContent, model.getKeyword());

                    }
                });
        return superManager.selectPageResultVO(page, wrap, model);
    }

    @Override
    public List<ChiefWorkOrderItemResultVO> selectListResultVO(ChiefWorkOrderItemPageQuery model) {
        List<ChiefWorkOrderItemResultVO> workOrderResultList = superManager.selectListResultVO(model);
        if ("办结".equals(model.getDisplayStatus()) || "已退回".equals(model.getDisplayStatus())) {
            setContentJson(workOrderResultList, model.getDisplayStatus(), model.getOrderNoList());
            return workOrderResultList;
        }
        return workOrderResultList;
    }

    @Override
    public void setContentJson(List<ChiefWorkOrderItemResultVO> workOrderResultList, String displayStatus, List<String> orderNoList) {
        List<ChiefWorkOrderDynamic> dynamicList = chiefWorkOrderDynamicManager.list(Wraps.<ChiefWorkOrderDynamic>lbQ().eq(ChiefWorkOrderDynamic::getProcessType, displayStatus.replaceAll("已", "")).in(ChiefWorkOrderDynamic::getOrderNo, orderNoList).orderByDesc(ChiefWorkOrderDynamic::getCreatedTime));
        Map<String, List<ChiefWorkOrderDynamic>> dynamicMap = dynamicList.stream()
                .collect(Collectors.groupingBy(
                        ChiefWorkOrderDynamic::getOrderNo,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
        workOrderResultList.forEach(t -> {
            List<ChiefWorkOrderDynamic> tempList = dynamicMap.get(t.getId().toString());
            if (!CollectionUtils.isEmpty(tempList)) t.setFinishOrBackDynamic(tempList.get(0));
        });
    }

    @Override
    @SneakyThrows
    public void exportTaskZip(List<String> orderNoList, HttpServletResponse response, String status) {
        // 1. 查询数据
        ChiefWorkOrderItemPageQuery chiefWorkOrderPageQuery = new ChiefWorkOrderItemPageQuery();
        chiefWorkOrderPageQuery.setOrderNoList(orderNoList);
        chiefWorkOrderPageQuery.setDisplayStatus(status);
        List<ChiefWorkOrderItemResultVO> chiefWorkOrderResultVOS = this.selectListResultVO(chiefWorkOrderPageQuery);
        List<ChiefWorkOrderExport> chiefWorkOrderExports = BeanUtil.copyToList(chiefWorkOrderResultVOS, ChiefWorkOrderExport.class);
        // 2. 准备文件名和填充数据
        Path execlPath = null;
        String fileNamePrefix = "导出文件";
        if (Objects.equals(status, "办结")) {
            ArgumentAssert.notNull(workExportFolderProperty.getChiefWorkFinishExcelPath(), "未配置 Excel 模板路径");
            execlPath = Paths.get(workExportFolderProperty.getChiefWorkFinishExcelPath());
            fileNamePrefix = "办结文件导出";
        }
        if (Objects.equals(status, "已退回")) {
            ArgumentAssert.notNull(workExportFolderProperty.getChiefWorkBackExcelPath(), "未配置 Excel 模板路径");
            execlPath = Paths.get(workExportFolderProperty.getChiefWorkBackExcelPath());
            fileNamePrefix = "退回文件导出";
        }
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
                // 尝试从 URL 中截取文件名
                // 通常 URL 格式为 .../filename.ext?query...
                String path = new java.net.URL(url).getPath();
                String tempName = path.substring(path.lastIndexOf('/') + 1);
                if (StringUtils.isNotBlank(tempName)) {
                    fileName = java.net.URLDecoder.decode(tempName, StandardCharsets.UTF_8.name());
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return fileName;
    }

    @Override
    public IPage<ChiefWorkOrderItemResultVO> findCommentedPageResultVO(PageParams<ChiefWorkOrderItemPageQuery> params) {
        IPage<ChiefWorkOrderItem> page = params.buildPage(ChiefWorkOrderItem.class);
        ChiefWorkOrderItemPageQuery model = params.getModel();
        Map<String, Object> extra = params.getExtra();
        LbQueryWrap<ChiefWorkOrderItem> wrap = Wraps.lbq(null, extra, ChiefWorkOrderItem.class);
        wrap.like(ChiefWorkOrderItem::getWorkOrderNo, model.getWorkOrderNo())
                .like(ChiefWorkOrderItem::getTitle, model.getTitle())
                .like(ChiefWorkOrderItem::getAppealContent, model.getAppealContent())
                .like(ChiefWorkOrderItem::getAppealType, model.getAppealType())
                .like(ChiefWorkOrderItem::getContactPhone, model.getContactPhone())
                .like(ChiefWorkOrderItem::getSettleCondition, model.getSettleCondition())
                .like(ChiefWorkOrderItem::getBatchNo, model.getBatchNo())
                .and(model.getIsJointQuery(), w -> {
                    if (model.getIsJointQuery() && StringUtils.isNotBlank(model.getKeyword())) {
                        w.like(ChiefWorkOrderItem::getWorkOrderNo, model.getKeyword())
                                .or()
                                .like(ChiefWorkOrderItem::getTitle, model.getKeyword())
                                .or()
                                .like(ChiefWorkOrderItem::getAppealContent, model.getKeyword());
                    }
                });


        return superManager.selectCommentedResultVO(page, wrap, model);
    }

    @Override
    public IPage<ChiefWorkOrderItemResultVO> findNotCommentPageResultVO(PageParams<ChiefWorkOrderItemPageQuery> params) {
        IPage<ChiefWorkOrderItem> page = params.buildPage(ChiefWorkOrderItem.class);
        ChiefWorkOrderItemPageQuery model = params.getModel();
        Map<String, Object> extra = params.getExtra();
        LbQueryWrap<ChiefWorkOrderItem> wrap = Wraps.lbq(null, extra, ChiefWorkOrderItem.class);
        wrap.like(ChiefWorkOrderItem::getWorkOrderNo, model.getWorkOrderNo())
                .like(ChiefWorkOrderItem::getTitle, model.getTitle())
                .like(ChiefWorkOrderItem::getAppealContent, model.getAppealContent())
                .like(ChiefWorkOrderItem::getAppealType, model.getAppealType())
                .like(ChiefWorkOrderItem::getContactPhone, model.getContactPhone())
                .like(ChiefWorkOrderItem::getSettleCondition, model.getSettleCondition())
                .like(ChiefWorkOrderItem::getBatchNo, model.getBatchNo())
                .and(model.getIsJointQuery(), w -> {
                    if (model.getIsJointQuery() && StringUtils.isNotBlank(model.getKeyword())) {
                        w.like(ChiefWorkOrderItem::getWorkOrderNo, model.getKeyword())
                                .or()
                                .like(ChiefWorkOrderItem::getTitle, model.getKeyword())
                                .or()
                                .like(ChiefWorkOrderItem::getAppealContent, model.getKeyword());
                    }
                });

        return superManager.selectNotCommentResultVO(page, wrap, model);
    }

    @Override
    public IPage<ChiefWorkOrderItemResultVO> selectOrderAllConditions(PageParams<ChiefWorkOrderItemPageQuery> params) {
        IPage<ChiefWorkOrderItem> page = params.buildPage(ChiefWorkOrderItem.class);
        ChiefWorkOrderItemPageQuery model = params.getModel();
        Map<String, Object> extra = params.getExtra();
        LbQueryWrap<ChiefWorkOrderItem> wrap = Wraps.lbq(null, extra, ChiefWorkOrderItem.class);
        wrap.like(ChiefWorkOrderItem::getWorkOrderNo, model.getWorkOrderNo())
                .eq(ChiefWorkOrderItem::getBatchNo, model.getBatchNo())
                .like(ChiefWorkOrderItem::getTitle, model.getTitle())
                .like(ChiefWorkOrderItem::getAppealContent, model.getAppealContent())
                .like(ChiefWorkOrderItem::getAppealType, model.getAppealType())
                .like(ChiefWorkOrderItem::getContactPhone, model.getContactPhone())
                .like(ChiefWorkOrderItem::getSettleCondition, model.getSettleCondition())
                .like(ChiefWorkOrderItem::getBatchNo, model.getBatchNo())
                .between(model.getLastOperateTimeStart() != null && model.getLastOperateTimeEnd() != null ,ChiefWorkOrderItem::getLastOperateTime, model.getLastOperateTimeStart(), model.getLastOperateTimeEnd());
        return superManager.selectOrderAllConditions(page, wrap, model);
    }

    @Override
    public List<ChiefWorkOrderItemResultVO> selectOrderAllConditionsList(ChiefWorkOrderItemPageQuery params) {
        LbQueryWrap<ChiefWorkOrderItem> wrap = Wraps.lbq(null,null, ChiefWorkOrderItem.class);
        wrap.like(ChiefWorkOrderItem::getWorkOrderNo, params.getWorkOrderNo())
                .eq(ChiefWorkOrderItem::getBatchNo, params.getBatchNo())
                .like(ChiefWorkOrderItem::getTitle, params.getTitle())
                .like(ChiefWorkOrderItem::getAppealContent, params.getAppealContent())
                .like(ChiefWorkOrderItem::getAppealType, params.getAppealType())
                .like(ChiefWorkOrderItem::getContactPhone, params.getContactPhone())
                .like(ChiefWorkOrderItem::getSettleCondition, params.getSettleCondition());
        return superManager.selectOrderAllConditions(wrap, params);
    }

    @Override
    public void getFinishOrBackContentJson(List<ChiefWorkOrderItemResultVO> records, ChiefWorkOrderItemPageQuery model) {
        if (CollectionUtils.isEmpty(records)) return;
        List<String> orderNoList = records.stream().map(ChiefWorkOrderItemResultVO::getWorkOrderNo).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(orderNoList)) {
            setContentJson(records, model.getDisplayStatus(), orderNoList);
        }
    }

    @Override
    public Long getWorkOrderCount(String displayStatus, String roleCode, String leadUnitId) {
        return superManager.getWorkOrderCount(displayStatus, roleCode, leadUnitId);
    }

    @Override
    public List<SignCategoryIsNullNormalWorkOrderResultVO> groupByCategoryWorkOrderCount(String roleCode, String leadUnitId) {
        return superManager.groupByCategoryWorkOrderCount(roleCode, leadUnitId);
    }

    @Override
    public Long signCategoryIsNull() {
        return superManager.signCategoryIsNull();
    }

    @Override
    public List<NormalWorkOrderRankingResultVO> getRanking() {
        return superManager.getRanking();
    }
}
