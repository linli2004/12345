package top.tangyh.lamp.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson2.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.lamp.Constant;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.entity.NormalWorkOrderExcel;
import top.tangyh.lamp.base.entity.NormalWorkOrderTask;
import top.tangyh.lamp.base.entity.WorkOrderDynamic;
import top.tangyh.lamp.base.manager.NormalWorkOrderManager;
import top.tangyh.lamp.base.manager.NormalWorkOrderTaskManager;
import top.tangyh.lamp.base.manager.WorkOrderDynamicManager;
import top.tangyh.lamp.base.service.NormalWorkOrderService;
import top.tangyh.lamp.base.vo.query.NormalWorkOrderPageQuery;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderRankingResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderResultVO;
import top.tangyh.lamp.base.vo.result.SignCategoryIsNullNormalWorkOrderResultVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;
import top.tangyh.lamp.common.constant.DsConstant;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 业务实现类
 * 普通工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:47:40
 * @create [2026-03-03 11:47:40] [lunar] [代码生成器生成]
 */
@DS(DsConstant.BASE_TENANT)
@Slf4j
@RequiredArgsConstructor
@Service
public class NormalWorkOrderServiceImpl extends SuperServiceImpl<NormalWorkOrderManager, Long, NormalWorkOrder> implements NormalWorkOrderService {
    private final NormalWorkOrderTaskManager normalWorkOrderTaskManager;
    private final WorkOrderDynamicManager workOrderDynamicManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importNormalWorkOrder(InputStream inputStream, List<String> errorOrderNoList, NormalWorkOrderTaskActionVO actionVO) {
        List<NormalWorkOrder> normalWorkOrderList = Lists.newArrayList();
        List<NormalWorkOrderTask> normalWorkOrderTaskList = Lists.newArrayList();
        List<WorkOrderDynamic> workOrderDynamicList = Lists.newArrayList();
        EasyExcel.read(inputStream, NormalWorkOrderExcel.class, new PageReadListener<NormalWorkOrderExcel>(dataList -> {
            for (NormalWorkOrderExcel cell : dataList) {
                try {
                    log.info("读取到一条数据{}", JSON.toJSONString(cell));
                    NormalWorkOrder normalWorkOrder = BeanUtil.toBean(cell, NormalWorkOrder.class);
                    normalWorkOrder.setIsDifficult(!"普通" .equals(cell.getIsDifficult()));
                    //查询是否存在此工单编号；如果是重复的工单编号 就更新
                    List<NormalWorkOrder> orderTempList = superManager.list(Wraps.<NormalWorkOrder>lbQ().eq(NormalWorkOrder::getOrderNo, normalWorkOrder.getOrderNo()));
                    if (!CollectionUtils.isEmpty(orderTempList)) normalWorkOrder.setId(orderTempList.get(0).getId());
                    normalWorkOrderList.add(normalWorkOrder);
                    //如果是重复的工单编号将老数据valid置0(无效),插入新task
                    List<NormalWorkOrderTask> taskTempList = normalWorkOrderTaskManager.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getOrderNo, normalWorkOrder.getOrderNo()));
                    if (!CollectionUtils.isEmpty(taskTempList)) {
                        taskTempList.forEach(t -> t.setValid(Constant.TASK_INVALID));
                        normalWorkOrderTaskList.addAll(taskTempList);
                    }
                    NormalWorkOrderTask taskTemp = new NormalWorkOrderTask();
                    taskTemp.setOrderNo(normalWorkOrder.getOrderNo());
                    normalWorkOrderTaskList.add(taskTemp);
                    //存入办理动态
                    WorkOrderDynamic dynamicTemp = BeanUtil.toBean(actionVO, WorkOrderDynamic.class);
                    dynamicTemp.setOrderNo(normalWorkOrder.getOrderNo());
                    dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_NOT_SIGN);
                    dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_NOT_SIGN));
                    workOrderDynamicList.add(dynamicTemp);
                } catch (Exception e) {
                    errorOrderNoList.add(cell.getOrderNo());
                }
            }
        })).sheet().doRead();
        superManager.saveOrUpdateBatch(normalWorkOrderList);
        normalWorkOrderTaskManager.saveOrUpdateBatch(normalWorkOrderTaskList);
        //存完task, 拿taskId
        Map<String, Long> orderNoTaskIdMap = normalWorkOrderTaskList.stream()
                .filter(task -> !Constant.TASK_INVALID.equals(task.getValid()))
                .collect(Collectors.toMap(
                        NormalWorkOrderTask::getOrderNo,
                        NormalWorkOrderTask::getId,
                        (v1, v2) -> v2
                ));
        workOrderDynamicList.forEach(t -> t.setTaskId(orderNoTaskIdMap.get(t.getOrderNo())));
        workOrderDynamicManager.saveBatch(workOrderDynamicList);
        log.info("工单导入失败数据:{}", JSON.toJSONString(errorOrderNoList));
    }

    @Override
    public IPage<NormalWorkOrderResultVO> findPageResultVO(PageParams<NormalWorkOrderPageQuery> params) {
        IPage<NormalWorkOrder> page = params.buildPage(NormalWorkOrder.class);
        NormalWorkOrderPageQuery model = params.getModel();
        Map<String, Object> extra = params.getExtra();
        LbQueryWrap<NormalWorkOrder> wrap = Wraps.lbq(null, extra, NormalWorkOrder.class);
        wrap.like(NormalWorkOrder::getOrderNo, model.getOrderNo())
                .like(NormalWorkOrder::getOrderTitle, model.getOrderTitle())
                .like(NormalWorkOrder::getOrderContent, model.getOrderContent())
                .like(NormalWorkOrder::getAppealType, model.getAppealType())
                .like(NormalWorkOrder::getUrgency, model.getUrgency())
                .like(NormalWorkOrder::getSourceDeptName, model.getSourceDeptName())
                .like(NormalWorkOrder::getChannel, model.getChannel())
                .like(NormalWorkOrder::getHelperName, model.getHelperName())
                .like(NormalWorkOrder::getContactPhone, model.getContactPhone())
                .like(NormalWorkOrder::getGender, model.getGender())
                .like(NormalWorkOrder::getIncidentLocation, model.getIncidentLocation())
                .like(NormalWorkOrder::getAddress, model.getAddress())
                .eq(NormalWorkOrder::getIsDifficult, model.getIsDifficult())
                .like(NormalWorkOrder::getHostDeptName, model.getHostDeptName())
                .like(NormalWorkOrder::getAssistDeptName, model.getAssistDeptName())
                .like(NormalWorkOrder::getCcDeptName, model.getCcDeptName())
                .eq(NormalWorkOrder::getSettleCondition, model.getSettleCondition())
                .and(model.getIsJointQuery(), w -> {
                    if (model.getIsJointQuery() && StringUtils.isNotBlank(model.getKeyword())) {
                        w.like(NormalWorkOrder::getOrderNo, model.getKeyword())
                                .or()
                                .like(NormalWorkOrder::getOrderTitle, model.getKeyword())
                                .or()
                                .like(NormalWorkOrder::getOrderContent, model.getKeyword())
                                .or()
                                .like(NormalWorkOrder::getAddress, model.getKeyword());
                    }
                });
        return superManager.selectPageResultVO(page, wrap, model);
    }

    @Override
    public List<NormalWorkOrderResultVO> selectListResultVO(NormalWorkOrderPageQuery model) {
        List<NormalWorkOrderResultVO> workOrderResultList = superManager.selectListResultVO(model);
        if ("办结" .equals(model.getDisplayStatus()) || "退回" .equals(model.getDisplayStatus())) {
            setContentJson(workOrderResultList, model.getDisplayStatus(), model.getOrderNoList());
            return workOrderResultList;
        }
        return workOrderResultList;
    }

    void setContentJson(List<NormalWorkOrderResultVO> workOrderResultList, String displayStatus, List<String> orderNoList) {
        List<WorkOrderDynamic> dynamicList = workOrderDynamicManager.list(Wraps.<WorkOrderDynamic>lbQ().eq(WorkOrderDynamic::getProcessType, displayStatus).in(WorkOrderDynamic::getOrderNo, orderNoList));
        Map<String, List<WorkOrderDynamic>> dynamicMap = dynamicList.stream()
                .collect(Collectors.groupingBy(
                        WorkOrderDynamic::getOrderNo,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        workOrderResultList.forEach(t -> {
            List<WorkOrderDynamic> tempList = dynamicMap.get(t.getOrderNo());
            if (!CollectionUtils.isEmpty(tempList)) t.setFinishOrBackContentJson(tempList.get(0).getContentJson());
        });
    }


    @Override
    @SneakyThrows
    public void exportTaskZip(@RequestBody List<Long> idList, HttpServletResponse response) {
        String fileName = URLEncoder.encode("项目资料包.zip", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        List<NormalWorkOrder> normalWorkOrders = superManager.listByIds(idList);
        List<NormalWorkOrderExcel> normalWorkOrderExcels = BeanUtil.copyToList(normalWorkOrders, NormalWorkOrderExcel.class);
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {

            ZipEntry excelEntry = new ZipEntry("工单.xlsx");
            zos.putNextEntry(excelEntry);
            EasyExcel.write(zos, NormalWorkOrderExcel.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("Sheet1")
                    .doWrite(normalWorkOrderExcels);
            zos.closeEntry(); // 完成当前文件的写入

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


