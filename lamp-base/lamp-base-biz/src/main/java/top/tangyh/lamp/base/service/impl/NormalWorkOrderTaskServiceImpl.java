package top.tangyh.lamp.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.AuditNodeCodeEnum;
import top.tangyh.lamp.Constant;
import top.tangyh.lamp.NoticeNodeCodeEnum;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.entity.NormalWorkOrderTask;
import top.tangyh.lamp.base.entity.WorkOrderDynamic;
import top.tangyh.lamp.base.manager.NormalWorkOrderManager;
import top.tangyh.lamp.base.manager.NormalWorkOrderTaskManager;
import top.tangyh.lamp.base.manager.WorkOrderDynamicManager;
import top.tangyh.lamp.base.service.NormalWorkOrderTaskService;
import top.tangyh.lamp.base.service.user.BaseEmployeeService;
import top.tangyh.lamp.base.vo.result.user.BaseEmployeeResultVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.model.entity.system.SysUser;
import top.tangyh.lamp.msg.biz.MsgBiz;
import top.tangyh.lamp.msg.vo.update.ExtendMsgPublishVO;

import java.util.*;
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
    private final MsgBiz msgBiz;
    private final BaseEmployeeService baseEmployeeService;

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
    public boolean batchOrderCategory(NormalWorkOrderTaskActionVO workOrderVO) {
        return normalWorkOrderManager.update(Wrappers.<NormalWorkOrder>lambdaUpdate()
                .set(NormalWorkOrder::getOrderCategoryId, workOrderVO.getOrderCategoryId())
                .set(NormalWorkOrder::getOrderCategoryName, workOrderVO.getOrderCategoryName())
                .in(NormalWorkOrder::getOrderNo, workOrderVO.getOrderNoList()));
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
            List<NormalWorkOrderTask> currentLoopTaskList = Lists.newArrayList();
            currentLoopTaskList.add(normalWorkOrderTask);
            if (leadUnitIdArray.length > 1) {
                for (int i = 1; i < leadUnitIdArray.length; i++) {
                    //新增task
                    NormalWorkOrderTask taskTemp = BeanUtil.copyProperties(normalWorkOrderTask, NormalWorkOrderTask.class, "id");
                    taskTemp.setLeadUnitId(Long.valueOf(leadUnitIdArray[i]));
                    currentLoopTaskList.add(taskTemp);
                }
                //如果是全部结案的情况，为镇级新增一条task,它的level是0，当前循环其他task level为1
                if (Constant.SETTLE_CONDITION_ALL.equals(processingVO.getSettleCondition())) {
                    currentLoopTaskList.forEach(t -> t.setLevel(Constant.TASK_LEVEL_1));
                    NormalWorkOrderTask taskTemp = BeanUtil.copyProperties(normalWorkOrderTask, NormalWorkOrderTask.class, "id");
                    taskTemp.setLeadUnitId(1L);
                    taskTemp.setLevel(Constant.TASK_LEVEL_0);
                    currentLoopTaskList.add(taskTemp);
                }
            }
            normalWorkOrderTaskList.addAll(currentLoopTaskList);
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
            ArgumentAssert.notEmpty(taskTempList, "工单编号有误");
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
        //更新normal_work_order_task node_code 3.2 办结  处理多个子工单
        List<NormalWorkOrderTask> taskTempList = superManager.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).eq(NormalWorkOrderTask::getOrderNo, finishVO.getOrderNo()));
        ArgumentAssert.notEmpty(taskTempList, "工单编号有误");
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean basicDirectorAuditNormalWorkOrder(NormalWorkOrderTaskActionVO auditVO) {
        //新增 基层负责人审批的办理动态
        WorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(auditVO, WorkOrderDynamic.class, "id");
        dynamicTemp.setOrderNo(auditVO.getOrderNo());
        dynamicTemp.setTaskId(auditVO.getId());
        String nodeCode = AuditNodeCodeEnum.getNodeCode(auditVO.getAuditType(), auditVO.getAuditResult(), Constant.ROLE_CODE_DEPT_DIRECTOR);
        dynamicTemp.setNodeCode(nodeCode);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(nodeCode));
        workOrderDynamicManager.save(dynamicTemp);
        return superManager.update(Wrappers.<NormalWorkOrderTask>lambdaUpdate()
                .set(NormalWorkOrderTask::getCurrentNodeCode, nodeCode)
                .eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(NormalWorkOrderTask::getId, auditVO.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean basicLeaderAuditNormalWorkOrder(NormalWorkOrderTaskActionVO auditVO) {
        //新增 基层领导审批的办理动态
        WorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(auditVO, WorkOrderDynamic.class, "id");
        dynamicTemp.setOrderNo(auditVO.getOrderNo());
        dynamicTemp.setTaskId(auditVO.getId());
        String nodeCode = AuditNodeCodeEnum.getNodeCode(auditVO.getAuditType(), auditVO.getAuditResult(), Constant.ROLE_CODE_DEPT_LEADER);
        dynamicTemp.setNodeCode(nodeCode);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(nodeCode));
        workOrderDynamicManager.save(dynamicTemp);
        NormalWorkOrder workOrderTemp = normalWorkOrderManager.getOne(Wraps.<NormalWorkOrder>lbQ().eq(NormalWorkOrder::getOrderNo, auditVO.getOrderNo()));
        superManager.update(Wrappers.<NormalWorkOrderTask>lambdaUpdate()
                .set(NormalWorkOrderTask::getCurrentNodeCode, nodeCode)
                .set(!Constant.SETTLE_CONDITION_ALL.equals(workOrderTemp.getSettleCondition()), NormalWorkOrderTask::getLevel, Constant.TASK_LEVEL_0)
                .eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(NormalWorkOrderTask::getId, auditVO.getId()));
        List<NormalWorkOrderTask> taskTempList = superManager.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).eq(NormalWorkOrderTask::getOrderNo, auditVO.getOrderNo()));
        ArgumentAssert.notEmpty(taskTempList, "工单编号有误");
        if (Constant.SETTLE_CONDITION_ALL.equals(workOrderTemp.getSettleCondition())) {
            //除lead_unit_id=1外的task node都为15.1  将lead_unit_id=1的task 置15.1
            //除lead_unit_id=1外的task node都为12.1  将lead_unit_id=1的task 置12.1
            if (Constant.NODE_CODE_BASIC_BACK_LEADER_APPROVE.equals(nodeCode) || Constant.NODE_CODE_BASIC_FINAL_LEADER_APPROVE.equals(nodeCode)) {
                updateTownTaskNodeCode(taskTempList, nodeCode);
            }
        } else {
            //如果是 退回15.1 通过， 优先展示，其他非15.1的task level 置1
            if (Constant.NODE_CODE_BASIC_BACK_LEADER_APPROVE.equals(nodeCode)) {
                List<NormalWorkOrderTask> taskList = taskTempList.stream().filter(item -> !Objects.equals(Constant.NODE_CODE_BASIC_BACK_LEADER_APPROVE, item.getCurrentNodeCode())).toList();
                taskList.forEach(t -> t.setLevel(Constant.TASK_LEVEL_1));
                superManager.updateBatchById(taskList);
            }
            //如果是 结案12.1 通过，其他task不存在15.1，将其他的task level 置1；存在则本条task level置1
            if (Constant.NODE_CODE_BASIC_FINAL_LEADER_APPROVE.equals(nodeCode)) {
                List<NormalWorkOrderTask> taskList = taskTempList.stream().filter(item -> Objects.equals(Constant.NODE_CODE_BASIC_BACK_LEADER_APPROVE, item.getCurrentNodeCode())).toList();
                if (!CollectionUtils.isEmpty(taskList)) {
                    superManager.update(Wrappers.<NormalWorkOrderTask>lambdaUpdate()
                            .set(NormalWorkOrderTask::getLevel, Constant.TASK_LEVEL_1)
                            .eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID)
                            .eq(NormalWorkOrderTask::getId, auditVO.getId()));
                } else {
                    List<NormalWorkOrderTask> list = taskTempList.stream().filter(item -> !Objects.equals(Constant.NODE_CODE_BASIC_FINAL_LEADER_APPROVE, item.getCurrentNodeCode())).toList();
                    list.forEach(t -> t.setLevel(Constant.TASK_LEVEL_1));
                    superManager.updateBatchById(list);
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean townBasicBackNormalWorkOrder(NormalWorkOrderTaskActionVO backVO) {
        //所有task node 变13.3, level都变0
        //新增 镇级退回的办理动态
        WorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(backVO, WorkOrderDynamic.class, "id");
        dynamicTemp.setOrderNo(backVO.getOrderNo());
        dynamicTemp.setTaskId(backVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_BASIC_BACK);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_BASIC_BACK));
        workOrderDynamicManager.save(dynamicTemp);
        return superManager.update(Wrappers.<NormalWorkOrderTask>lambdaUpdate()
                .set(NormalWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_TOWN_BASIC_BACK)
                .set(NormalWorkOrderTask::getLevel, Constant.TASK_LEVEL_0)
                .eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(NormalWorkOrderTask::getOrderNo, backVO.getOrderNo()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean townBasicFinishNormalWorkOrder(NormalWorkOrderTaskActionVO finishVO) {
        //所有task node 变13.1, level都变0
        //新增 镇级退回的办理动态
        WorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(finishVO, WorkOrderDynamic.class, "id");
        dynamicTemp.setOrderNo(finishVO.getOrderNo());
        dynamicTemp.setTaskId(finishVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_BASIC_FINAL);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_BASIC_FINAL));
        workOrderDynamicManager.save(dynamicTemp);
        return superManager.update(Wrappers.<NormalWorkOrderTask>lambdaUpdate()
                .set(NormalWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_TOWN_BASIC_FINAL)
                .set(NormalWorkOrderTask::getLevel, Constant.TASK_LEVEL_0)
                .eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(NormalWorkOrderTask::getOrderNo, finishVO.getOrderNo()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean revokeNormalWorkOrder(NormalWorkOrderTaskActionVO revokeVO) {
        superManager.update(Wrappers.<NormalWorkOrderTask>lambdaUpdate()
                .set(NormalWorkOrderTask::getValid, Constant.TASK_INVALID)
                .eq(NormalWorkOrderTask::getOrderNo, revokeVO.getOrderNo()));
        NormalWorkOrderTask taskTemp = new NormalWorkOrderTask();
        taskTemp.setOrderNo(revokeVO.getOrderNo());
        taskTemp.setCurrentNodeCode(Constant.NODE_CODE_TOWN_SIGN);
        superManager.save(taskTemp);
        //存入办理动态
        WorkOrderDynamic dynamicTemp = BeanUtil.toBean(revokeVO, WorkOrderDynamic.class);
        dynamicTemp.setOrderNo(revokeVO.getOrderNo());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_SIGN);
        dynamicTemp.setProcessType("撤回");
        return workOrderDynamicManager.save(dynamicTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean urgeNormalWorkOrder(NormalWorkOrderTaskActionVO urgeVO) {
        NormalWorkOrder workOrderTemp = normalWorkOrderManager.getOne(Wraps.<NormalWorkOrder>lbQ().eq(NormalWorkOrder::getOrderNo, urgeVO.getOrderNo()));
        List<NormalWorkOrderTask> taskTempList = superManager.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).eq(NormalWorkOrderTask::getOrderNo, urgeVO.getOrderNo()));
        ArgumentAssert.notEmpty(taskTempList, "工单编号有误");
        List<String> employeeId = Lists.newArrayList();
        String titleTemplate = "请及时处理工单【%s】";
        taskTempList.forEach(taskTemp -> {
            String roleCode = NoticeNodeCodeEnum.getRoleCode(taskTemp.getCurrentNodeCode());
            if (StringUtils.isNotBlank(roleCode)) {
                List<BaseEmployeeResultVO> employeeList = baseEmployeeService.getEmployeeIdByRoleCodeAndOrgId(roleCode, taskTemp.getLeadUnitId());
                if (!CollectionUtils.isEmpty(employeeList)) {
                    employeeId.addAll(employeeList.stream().map(e -> String.valueOf(e.getId())).toList());
                }
            }
        });
        ExtendMsgPublishVO data = new ExtendMsgPublishVO();
        data.setTitle(String.format(titleTemplate, workOrderTemp.getOrderTitle()));
        data.setContent(String.format(titleTemplate, workOrderTemp.getOrderTitle()));
        data.setRemindMode("03");
        data.setRecipientList(employeeId);
        return msgBiz.publish(data, new SysUser());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean againProcessingNormalWorkOrder(NormalWorkOrderTaskActionVO processingVO) {
        List<NormalWorkOrderTask> normalWorkOrderTaskList = Lists.newArrayList();
        String[] leadUnitIdArray = processingVO.getLeadUnitIds().split(",");
        List<String> list = new ArrayList<>(Arrays.stream(leadUnitIdArray).toList());
        list.add("1"); //unit_id=1的(全部结案的主task)
        ArgumentAssert.notEmpty(leadUnitIdArray, "主办单位不可为空");
        //将需要重新交办的task valid置0  包含unit_id=1的(全部结案的主task)
        superManager.update(Wrappers.<NormalWorkOrderTask>lambdaUpdate().set(NormalWorkOrderTask::getValid, Constant.TASK_INVALID).eq(NormalWorkOrderTask::getOrderNo, processingVO.getOrderNo()).in(NormalWorkOrderTask::getLeadUnitId, list));
        for (String leadUnitId : leadUnitIdArray) {//新增task
            NormalWorkOrderTask taskTemp = BeanUtil.copyProperties(processingVO, NormalWorkOrderTask.class, "id");
            taskTemp.setLeadUnitId(Long.valueOf(leadUnitId));
            taskTemp.setCurrentNodeCode(Constant.NODE_CODE_TOWN_AGAIN_PROCESSING);
            normalWorkOrderTaskList.add(taskTemp);
        }
        if (leadUnitIdArray.length > 1 && Constant.SETTLE_CONDITION_ALL.equals(processingVO.getSettleCondition())) {
            normalWorkOrderTaskList.forEach(t -> t.setLevel(Constant.TASK_LEVEL_1));
            NormalWorkOrderTask taskTemp = BeanUtil.copyProperties(processingVO, NormalWorkOrderTask.class, "id");
            taskTemp.setCurrentNodeCode(Constant.NODE_CODE_TOWN_AGAIN_PROCESSING);
            taskTemp.setLeadUnitId(1L);
            taskTemp.setLevel(Constant.TASK_LEVEL_0);
            normalWorkOrderTaskList.add(taskTemp);
        }
        //新增 交办的办理动态
        WorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(processingVO, WorkOrderDynamic.class, "id");
        dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_AGAIN_PROCESSING);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_AGAIN_PROCESSING));
        //更改normal_work_order 工单分类id 工单分类名称 结案条件 是否允许退回 允许退回时间 允许批示
        NormalWorkOrder workOrderTemp = normalWorkOrderManager.getOne(Wraps.<NormalWorkOrder>lbQ().eq(NormalWorkOrder::getOrderNo, processingVO.getOrderNo()));
        workOrderTemp.setOrderCategoryId(processingVO.getOrderCategoryId());
        workOrderTemp.setOrderCategoryName(processingVO.getOrderCategoryName());
        workOrderTemp.setSettleCondition(processingVO.getSettleCondition());
        workOrderTemp.setAllowBack(processingVO.getAllowBack());
        workOrderTemp.setAllowBackTime(processingVO.getAllowBackTime());
        workOrderTemp.setAllowComment(processingVO.getAllowComment());
        superManager.saveOrUpdateBatch(normalWorkOrderTaskList);
        workOrderDynamicManager.save(dynamicTemp);
        return normalWorkOrderManager.updateById(workOrderTemp);
    }

    private void updateTownTaskNodeCode(List<NormalWorkOrderTask> taskTempList, String nodeCode) {
        long count = taskTempList.stream().filter(item -> nodeCode.equals(item.getCurrentNodeCode())).count();
        List<NormalWorkOrderTask> townTaskList = taskTempList.stream().filter(item -> Objects.equals(1L, item.getLeadUnitId())).toList();
        if (count + 1 == taskTempList.size()) {
            townTaskList.forEach(t -> t.setCurrentNodeCode(nodeCode));
            superManager.updateBatchById(townTaskList);
        } else {
            //存在1个task node 是15.1 则lead_unit_id=1的task node =15.1
            if (Constant.NODE_CODE_BASIC_BACK_LEADER_APPROVE.equals(nodeCode)) {
                townTaskList.forEach(t -> t.setCurrentNodeCode(nodeCode));
                superManager.updateBatchById(townTaskList);
            }
        }
    }
}


