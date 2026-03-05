package top.tangyh.lamp.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.Constant;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.entity.NormalWorkOrderTask;
import top.tangyh.lamp.base.entity.WorkOrderDynamic;
import top.tangyh.lamp.base.manager.NormalWorkOrderManager;
import top.tangyh.lamp.base.manager.NormalWorkOrderTaskManager;
import top.tangyh.lamp.base.manager.WorkOrderDynamicManager;
import top.tangyh.lamp.base.service.NormalWorkOrderTaskService;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;
import top.tangyh.lamp.common.constant.DsConstant;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 工单子表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:45:59
 * @create [2026-03-03 11:45:59] [lunar] [代码生成器生成]
 */
@DS(DsConstant.BASE_TENANT)
@Slf4j
@RequiredArgsConstructor
@Service
public class NormalWorkOrderTaskServiceImpl extends SuperServiceImpl<NormalWorkOrderTaskManager, Long, NormalWorkOrderTask> implements NormalWorkOrderTaskService {
    private final WorkOrderDynamicManager workOrderDynamicManager;
    private final NormalWorkOrderManager normalWorkOrderManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSignNormalWorkOrder(NormalWorkOrderTaskActionVO signVO) {
        List<WorkOrderDynamic> workOrderDynamicList = Lists.newArrayList();
        List<NormalWorkOrderTask> workOrderTaskList = superManager.list(Wraps.<NormalWorkOrderTask>lbQ()
                .eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID)
                .in(NormalWorkOrderTask::getOrderNo, signVO.getOrderNoList()));
        Map<String, Long> orderNoTaskIdMap = workOrderTaskList.stream()
                .collect(Collectors.toMap(
                        NormalWorkOrderTask::getOrderNo,
                        NormalWorkOrderTask::getId,
                        (v1, v2) -> v2
                ));
        //存入办理动态
        signVO.getOrderNoList().forEach(t -> {
            WorkOrderDynamic dynamicTemp = BeanUtil.toBean(signVO, WorkOrderDynamic.class);
            dynamicTemp.setOrderNo(t);
            dynamicTemp.setTaskId(orderNoTaskIdMap.get(t));
            dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_SIGN);
            dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_SIGN));
            workOrderDynamicList.add(dynamicTemp);
        });
        workOrderDynamicManager.saveBatch(workOrderDynamicList);
        return superManager.update(Wrappers.<NormalWorkOrderTask>lambdaUpdate()
                .set(NormalWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_TOWN_SIGN)
                .eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID)
                .in(NormalWorkOrderTask::getOrderNo, signVO.getOrderNoList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchProcessingNormalWorkOrder(List<NormalWorkOrderTaskActionVO> processingVOList) {
        List<NormalWorkOrderTask> normalWorkOrderTaskList = Lists.newArrayList();
        List<WorkOrderDynamic> workOrderDynamicList = Lists.newArrayList();
        List<NormalWorkOrder> workOrderList = Lists.newArrayList();
        processingVOList.forEach(processingVO -> {
            //如果有多个主办单位，新增normal_work_order_task  并且更改 业务节点编码 处理截止时间
            String[] leadUnitIdArray = processingVO.getLeadUnitIds().split(",");
            ArgumentAssert.notEmpty(leadUnitIdArray, "主办单位不可为空");
            NormalWorkOrderTask normalWorkOrderTask = BeanUtil.toBean(processingVO, NormalWorkOrderTask.class);
            normalWorkOrderTask.setCurrentNodeCode(Constant.NODE_CODE_TOWN_FIRST_PROCESSING);
            normalWorkOrderTask.setLeadUnitId(Long.valueOf(leadUnitIdArray[0]));
            normalWorkOrderTaskList.add(normalWorkOrderTask);
            if (leadUnitIdArray.length > 1) {
                for (int i = 1; i < leadUnitIdArray.length; i++) {
                    //新增task
                    NormalWorkOrderTask taskTemp = BeanUtil.copyProperties(normalWorkOrderTask, NormalWorkOrderTask.class, "id");
                    taskTemp.setLeadUnitId(Long.valueOf(leadUnitIdArray[i]));
                    normalWorkOrderTaskList.add(taskTemp);
                }
            }
            //新增 交办的办理动态
            WorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(processingVO, WorkOrderDynamic.class, "id");
            dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_FIRST_PROCESSING);
            dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_FIRST_PROCESSING));
            workOrderDynamicList.add(dynamicTemp);
            //更改normal_work_order 工单分类id 工单分类名称 结案条件 是否允许退回 允许退回时间 允许批示
            NormalWorkOrder workOrderTemp = normalWorkOrderManager.getOne(Wraps.<NormalWorkOrder>lbQ().eq(NormalWorkOrder::getOrderNo, processingVO.getOrderNo()));
            workOrderTemp.setOrderCategoryId(processingVO.getOrderCategoryId());
            workOrderTemp.setOrderCategoryName(processingVO.getOrderCategoryName());
            workOrderTemp.setSettleCondition(processingVO.getSettleCondition());
            workOrderTemp.setAllowBack(processingVO.getAllowBack());
            workOrderTemp.setAllowBackTime(processingVO.getAllowBackTime());
            workOrderTemp.setAllowComment(processingVO.getAllowComment());
            workOrderList.add(workOrderTemp);
        });
        superManager.saveOrUpdateBatch(normalWorkOrderTaskList);
        workOrderDynamicManager.saveBatch(workOrderDynamicList);
        return normalWorkOrderManager.updateBatchById(workOrderList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchBackNormalWorkOrder(List<NormalWorkOrderTaskActionVO> backVOList) {
        List<WorkOrderDynamic> workOrderDynamicList = Lists.newArrayList();
        List<NormalWorkOrderTask> normalWorkOrderTaskList = Lists.newArrayList();
        backVOList.forEach(backVO -> {
            //更新normal_work_order_task node_code 3.3 已退回 and 多个子工单
            List<NormalWorkOrderTask> taskTempList = superManager.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).eq(NormalWorkOrderTask::getOrderNo, backVO.getOrderNo()));
            taskTempList.forEach(t -> {
                t.setCurrentNodeCode(Constant.NODE_CODE_TOWN_BACK);
                normalWorkOrderTaskList.add(t);
            });
            //新增 退回的办理动态
            WorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(backVO, WorkOrderDynamic.class, "id");
            dynamicTemp.setTaskId(backVO.getId());
            dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_BACK);
            dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_BACK));
            workOrderDynamicList.add(dynamicTemp);
        });
        workOrderDynamicManager.saveBatch(workOrderDynamicList);
        return superManager.updateBatchById(normalWorkOrderTaskList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean finishNormalWorkOrder(NormalWorkOrderTaskActionVO finishVO) {
        List<NormalWorkOrderTask> normalWorkOrderTaskList = Lists.newArrayList();
        List<WorkOrderDynamic> workOrderDynamicList = Lists.newArrayList();
        List<NormalWorkOrder> workOrderList = Lists.newArrayList();
        //更改normal_work_order 工单分类id 工单分类名称
        NormalWorkOrder workOrderTemp = normalWorkOrderManager.getOne(Wraps.<NormalWorkOrder>lbQ().eq(NormalWorkOrder::getOrderNo, finishVO.getOrderNo()));
        workOrderTemp.setOrderCategoryId(finishVO.getOrderCategoryId());
        workOrderTemp.setOrderCategoryName(finishVO.getOrderCategoryName());
        workOrderList.add(workOrderTemp);
        //更新normal_work_order_task node_code 3.2 办结 todo 处理多个子工单
        List<NormalWorkOrderTask> taskTempList = superManager.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).eq(NormalWorkOrderTask::getOrderNo, finishVO.getOrderNo()));
        taskTempList.forEach(t -> {
            t.setCurrentNodeCode(Constant.NODE_CODE_TOWN_FINAL);
            normalWorkOrderTaskList.add(t);
        });
        //新增 办结的办理动态
        WorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(finishVO, WorkOrderDynamic.class, "id");
        dynamicTemp.setTaskId(finishVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_FINAL);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_FINAL));
        workOrderDynamicList.add(dynamicTemp);
        superManager.updateBatchById(normalWorkOrderTaskList);
        workOrderDynamicManager.saveBatch(workOrderDynamicList);
        return normalWorkOrderManager.updateBatchById(workOrderList);
    }

    @Override
    public boolean basicSignNormalWorkOrder(NormalWorkOrderTaskActionVO signVO) {
        //新增 基层签收的办理动态
        WorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(signVO, WorkOrderDynamic.class, "id");
        dynamicTemp.setOrderNo(signVO.getOrderNo());
        dynamicTemp.setTaskId(signVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_BASIC_SIGN);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_BASIC_SIGN));
        workOrderDynamicManager.save(dynamicTemp);
        //更改normal_work_order_task node_code 4 基层签收
        return superManager.update(Wrappers.<NormalWorkOrderTask>lambdaUpdate()
                .set(NormalWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_BASIC_SIGN)
                .eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(NormalWorkOrderTask::getId, signVO.getId()));
    }

    @Override
    public boolean basicBackNormalWorkOrder(NormalWorkOrderTaskActionVO backVO) {
        //新增 基层退回的办理动态
        WorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(backVO, WorkOrderDynamic.class, "id");
        dynamicTemp.setOrderNo(backVO.getOrderNo());
        dynamicTemp.setTaskId(backVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_BASIC_BACK);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_BASIC_BACK));
        workOrderDynamicManager.save(dynamicTemp);
        //更改normal_work_order_task node_code 5.3 基层退回
        return superManager.update(Wrappers.<NormalWorkOrderTask>lambdaUpdate()
                .set(NormalWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_BASIC_BACK)
                .eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(NormalWorkOrderTask::getId, backVO.getId()));
    }

    @Override
    public boolean basicFinishNormalWorkOrder(NormalWorkOrderTaskActionVO finishVO) {
        //新增 基层办结的办理动态
        WorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(finishVO, WorkOrderDynamic.class, "id");
        dynamicTemp.setOrderNo(finishVO.getOrderNo());
        dynamicTemp.setTaskId(finishVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_BASIC_FINAL);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_BASIC_FINAL));
        workOrderDynamicManager.save(dynamicTemp);
        //更改normal_work_order_task node_code 5.2 基层办结
        return superManager.update(Wrappers.<NormalWorkOrderTask>lambdaUpdate()
                .set(NormalWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_BASIC_FINAL)
                .eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(NormalWorkOrderTask::getId, finishVO.getId()));
    }
}


