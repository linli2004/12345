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
import top.tangyh.lamp.base.entity.*;
import top.tangyh.lamp.base.manager.ChiefWorkOrderDynamicManager;
import top.tangyh.lamp.base.manager.ChiefWorkOrderItemManager;
import top.tangyh.lamp.base.manager.ChiefWorkOrderManager;
import top.tangyh.lamp.base.manager.ChiefWorkOrderTaskManager;
import top.tangyh.lamp.base.service.ChiefWorkOrderTaskService;
import top.tangyh.lamp.base.service.user.BaseEmployeeService;
import top.tangyh.lamp.base.vo.query.ChiefWorkOrderItemPageQuery;
import top.tangyh.lamp.base.vo.result.user.BaseEmployeeResultVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.model.entity.system.SysUser;
import top.tangyh.lamp.msg.biz.MsgBiz;
import top.tangyh.lamp.msg.vo.update.ExtendMsgPublishVO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 督办工单子表
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
public class ChiefWorkOrderTaskServiceImpl extends SuperServiceImpl<ChiefWorkOrderTaskManager, Long, ChiefWorkOrderTask> implements ChiefWorkOrderTaskService {
    private final ChiefWorkOrderDynamicManager chiefWorkOrderDynamicManager;
    private final ChiefWorkOrderManager chiefWorkOrderManager;
    private final ChiefWorkOrderItemManager chiefWorkOrderItemManager;
    private final MsgBiz msgBiz;
    private final BaseEmployeeService baseEmployeeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSignChiefWorkOrder(NormalWorkOrderTaskActionVO signVO) {
        List<ChiefWorkOrderDynamic> workOrderDynamicList = Lists.newArrayList();
        List<ChiefWorkOrderTask> workOrderTaskList = superManager.list(Wraps.<ChiefWorkOrderTask>lbQ()
                .eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID)
                .in(ChiefWorkOrderTask::getOrderNo, signVO.getOrderNoList()));
        Map<String, Long> orderNoTaskIdMap = workOrderTaskList.stream()
                .collect(Collectors.toMap(
                        ChiefWorkOrderTask::getOrderNo,
                        ChiefWorkOrderTask::getId,
                        (v1, v2) -> v2
                ));
        //存入办理动态
        signVO.getOrderNoList().forEach(t -> {
            ChiefWorkOrderDynamic dynamicTemp = BeanUtil.toBean(signVO, ChiefWorkOrderDynamic.class);
            dynamicTemp.setOrderNo(t);
            dynamicTemp.setTaskId(orderNoTaskIdMap.get(t));
            dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_SIGN);
            dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_SIGN));
            workOrderDynamicList.add(dynamicTemp);
        });
        chiefWorkOrderDynamicManager.saveBatch(workOrderDynamicList);
        return superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate()
                .set(ChiefWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_TOWN_SIGN)
                .eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID)
                .in(ChiefWorkOrderTask::getOrderNo, signVO.getOrderNoList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchProcessingChiefWorkOrder(List<NormalWorkOrderTaskActionVO> processingVOList) {
        List<ChiefWorkOrderTask> normalWorkOrderTaskList = Lists.newArrayList();
        List<ChiefWorkOrderDynamic> workOrderDynamicList = Lists.newArrayList();
        List<ChiefWorkOrderItem> workOrderList = Lists.newArrayList();
        processingVOList.forEach(processingVO -> {
            //如果有多个主办单位，新增normal_work_order_task  并且更改 业务节点编码 处理截止时间
            String[] leadUnitIdArray = processingVO.getLeadUnitIds().split(",");
            ArgumentAssert.notEmpty(leadUnitIdArray, "主办单位不可为空");
            ChiefWorkOrderTask normalWorkOrderTask = BeanUtil.toBean(processingVO, ChiefWorkOrderTask.class);
            normalWorkOrderTask.setCurrentNodeCode(Constant.NODE_CODE_BASIC_SIGN);
            normalWorkOrderTask.setLeadUnitId(Long.valueOf(leadUnitIdArray[0]));
            List<ChiefWorkOrderTask> currentLoopTaskList = Lists.newArrayList();
            currentLoopTaskList.add(normalWorkOrderTask);
            if (leadUnitIdArray.length > 1) {
                for (int i = 1; i < leadUnitIdArray.length; i++) {
                    //新增task
                    ChiefWorkOrderTask taskTemp = BeanUtil.copyProperties(normalWorkOrderTask, ChiefWorkOrderTask.class, "id", "createdTime", "updatedTime");
                    taskTemp.setLeadUnitId(Long.valueOf(leadUnitIdArray[i]));
                    currentLoopTaskList.add(taskTemp);
                }
                //如果是全部结案的情况，为镇级新增一条task,它的level是0，当前循环其他task level为1
                if (Constant.SETTLE_CONDITION_ALL.equals(processingVO.getSettleCondition())) {
                    currentLoopTaskList.forEach(t -> t.setLevel(Constant.TASK_LEVEL_1));
                    ChiefWorkOrderTask taskTemp = BeanUtil.copyProperties(normalWorkOrderTask, ChiefWorkOrderTask.class, "id", "createdTime", "updatedTime");
                    taskTemp.setLeadUnitId(1L);
                    taskTemp.setLevel(Constant.TASK_LEVEL_0);
                    currentLoopTaskList.add(taskTemp);
                }
            }
            normalWorkOrderTaskList.addAll(currentLoopTaskList);
            //新增 交办的办理动态
            ChiefWorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(processingVO, ChiefWorkOrderDynamic.class, "id", "createdTime", "updatedTime");
            dynamicTemp.setNodeCode(Constant.NODE_CODE_BASIC_SIGN);
            dynamicTemp.setProcessType("交办");
            workOrderDynamicList.add(dynamicTemp);
            //更改normal_work_order 工单分类id 工单分类名称 结案条件 是否允许退回 允许退回时间 允许批示
            ChiefWorkOrderItem workOrderTemp = chiefWorkOrderItemManager.getOne(Wraps.<ChiefWorkOrderItem>lbQ().eq(ChiefWorkOrderItem::getId, processingVO.getOrderNo()));
            workOrderTemp.setOrderCategoryId(processingVO.getOrderCategoryId());
            workOrderTemp.setOrderCategoryName(processingVO.getOrderCategoryName());
            workOrderTemp.setSettleCondition(processingVO.getSettleCondition());
            workOrderTemp.setAllowBack(processingVO.getAllowBack());
            workOrderTemp.setAllowBackTime(processingVO.getAllowBackTime());
            workOrderTemp.setAllowComment(processingVO.getAllowComment());
            workOrderTemp.setCommentLeaderId(processingVO.getCommentLeaderId());
            workOrderList.add(workOrderTemp);
        });
        superManager.saveOrUpdateBatch(normalWorkOrderTaskList);
        chiefWorkOrderDynamicManager.saveBatch(workOrderDynamicList);
        return chiefWorkOrderItemManager.updateBatchById(workOrderList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchBackChiefWorkOrder(List<NormalWorkOrderTaskActionVO> backVOList) {
        List<ChiefWorkOrderDynamic> workOrderDynamicList = Lists.newArrayList();
        List<ChiefWorkOrderTask> normalWorkOrderTaskList = Lists.newArrayList();
        backVOList.forEach(backVO -> {
            //更新normal_work_order_task node_code 3.3 已退回 and 多个子工单
            List<ChiefWorkOrderTask> taskTempList = superManager.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).eq(ChiefWorkOrderTask::getOrderNo, backVO.getOrderNo()));
            ArgumentAssert.notEmpty(taskTempList, "工单编号有误");
            taskTempList.forEach(t -> {
                t.setCurrentNodeCode(Constant.NODE_CODE_TOWN_BACK);
                normalWorkOrderTaskList.add(t);
            });
            //新增 退回的办理动态
            ChiefWorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(backVO, ChiefWorkOrderDynamic.class, "id", "createdTime", "updatedTime");
            dynamicTemp.setTaskId(backVO.getId());
            dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_BACK);
            dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_BACK));
            workOrderDynamicList.add(dynamicTemp);
            //updateChiefWorkOrderStatus(backVO.getOrderNo());
        });
        chiefWorkOrderDynamicManager.saveBatch(workOrderDynamicList);
        boolean result =  superManager.updateBatchById(normalWorkOrderTaskList);
        if(result){
            backVOList.stream()
                    .map(NormalWorkOrderTaskActionVO::getOrderNo)
                    .distinct()
                    .forEach(this::updateChiefWorkOrderStatus);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean finishChiefWorkOrder(NormalWorkOrderTaskActionVO finishVO) {
        List<ChiefWorkOrderTask> normalWorkOrderTaskList = Lists.newArrayList();
        List<ChiefWorkOrderDynamic> workOrderDynamicList = Lists.newArrayList();
        List<ChiefWorkOrderItem> workOrderList = Lists.newArrayList();
        //更改normal_work_order 工单分类id 工单分类名称
        ChiefWorkOrderItem workOrderTemp = chiefWorkOrderItemManager.getOne(Wraps.<ChiefWorkOrderItem>lbQ().eq(ChiefWorkOrderItem::getId, finishVO.getOrderNo()));
        workOrderTemp.setOrderCategoryId(finishVO.getOrderCategoryId());
        workOrderTemp.setOrderCategoryName(finishVO.getOrderCategoryName());
        workOrderList.add(workOrderTemp);
        //更新normal_work_order_task node_code 3.2 办结  处理多个子工单
        List<ChiefWorkOrderTask> taskTempList = superManager.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).eq(ChiefWorkOrderTask::getOrderNo, finishVO.getOrderNo()));
        ArgumentAssert.notEmpty(taskTempList, "工单编号有误");
        taskTempList.forEach(t -> {
            t.setCurrentNodeCode(Constant.NODE_CODE_TOWN_FINAL);
            normalWorkOrderTaskList.add(t);
        });
        //新增 办结的办理动态
        ChiefWorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(finishVO, ChiefWorkOrderDynamic.class, "id", "createdTime", "updatedTime");
        dynamicTemp.setTaskId(finishVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_FINAL);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_FINAL));
        workOrderDynamicList.add(dynamicTemp);
        superManager.updateBatchById(normalWorkOrderTaskList);
        chiefWorkOrderDynamicManager.saveBatch(workOrderDynamicList);
        //updateChiefWorkOrderStatus(finishVO.getOrderNo());
        boolean result =  chiefWorkOrderItemManager.updateBatchById(workOrderList);
        if(result){
            updateChiefWorkOrderStatus(finishVO.getOrderNo());

        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean basicSignChiefWorkOrder(NormalWorkOrderTaskActionVO signVO) {
        //新增 基层签收的办理动态
        ChiefWorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(signVO, ChiefWorkOrderDynamic.class, "id", "createdTime", "updatedTime");
        dynamicTemp.setOrderNo(signVO.getOrderNo());
        dynamicTemp.setTaskId(signVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_BASIC_SIGN);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_BASIC_SIGN));
        chiefWorkOrderDynamicManager.save(dynamicTemp);
        //更改normal_work_order_task node_code 4 基层签收
        return superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate()
                .set(ChiefWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_BASIC_SIGN)
                .eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(ChiefWorkOrderTask::getId, signVO.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean basicBackChiefWorkOrder(NormalWorkOrderTaskActionVO backVO) {
        //判断是否可以退回
        ChiefWorkOrderItem workOrderTemp = chiefWorkOrderItemManager.getOne(Wraps.<ChiefWorkOrderItem>lbQ().eq(ChiefWorkOrderItem::getId, backVO.getOrderNo()));
        ArgumentAssert.isFalse(Constant.ALLOW_BACK_0.equals(workOrderTemp.getAllowBack()) || (Constant.ALLOW_BACK_2.equals(workOrderTemp.getAllowBack()) && LocalDateTime.now().isAfter(workOrderTemp.getAllowBackTime())), "此工单已不允许退回");
        //新增 基层退回的办理动态
        ChiefWorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(backVO, ChiefWorkOrderDynamic.class, "id", "createdTime", "updatedTime");
        dynamicTemp.setOrderNo(backVO.getOrderNo());
        dynamicTemp.setTaskId(backVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_BASIC_BACK);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_BASIC_BACK));
        chiefWorkOrderDynamicManager.save(dynamicTemp);
        //更改normal_work_order_task node_code 5.3 基层退回
        return superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate()
                .set(ChiefWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_BASIC_BACK)
                .set(ChiefWorkOrderTask::getLeadEmployeeId, backVO.getLeadEmployeeId())
                .eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(ChiefWorkOrderTask::getId, backVO.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean basicFinishChiefWorkOrder(NormalWorkOrderTaskActionVO finishVO) {
        //新增 基层办结的办理动态
        ChiefWorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(finishVO, ChiefWorkOrderDynamic.class, "id", "createdTime", "updatedTime");
        dynamicTemp.setOrderNo(finishVO.getOrderNo());
        dynamicTemp.setTaskId(finishVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_BASIC_FINAL);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_BASIC_FINAL));
        chiefWorkOrderDynamicManager.save(dynamicTemp);
        //更改normal_work_order_task node_code 5.2 基层办结
        return superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate()
                .set(ChiefWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_BASIC_FINAL)
                .set(ChiefWorkOrderTask::getLeadEmployeeId, finishVO.getLeadEmployeeId())
                .eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(ChiefWorkOrderTask::getId, finishVO.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean basicDirectorAuditChiefWorkOrder(NormalWorkOrderTaskActionVO auditVO) {
        //新增 基层负责人审批的办理动态
        ChiefWorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(auditVO, ChiefWorkOrderDynamic.class, "id", "createdTime", "updatedTime");
        dynamicTemp.setOrderNo(auditVO.getOrderNo());
        dynamicTemp.setTaskId(auditVO.getId());
        String nodeCode = AuditNodeCodeEnum.getNodeCode(auditVO.getAuditType(), auditVO.getAuditResult(), Constant.ROLE_CODE_DEPT_DIRECTOR);
        dynamicTemp.setNodeCode(nodeCode);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(nodeCode));
        chiefWorkOrderDynamicManager.save(dynamicTemp);
        return superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate()
                .set(ChiefWorkOrderTask::getCurrentNodeCode, nodeCode)
                .set(ChiefWorkOrderTask::getLeadEmployeeId, auditVO.getLeadEmployeeId())
                .eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(ChiefWorkOrderTask::getId, auditVO.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean basicLeaderAuditChiefWorkOrder(NormalWorkOrderTaskActionVO auditVO) {
        //新增 基层领导审批的办理动态
        ChiefWorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(auditVO, ChiefWorkOrderDynamic.class, "id", "createdTime", "updatedTime");
        dynamicTemp.setOrderNo(auditVO.getOrderNo());
        dynamicTemp.setTaskId(auditVO.getId());
        String nodeCode = AuditNodeCodeEnum.getNodeCode(auditVO.getAuditType(), auditVO.getAuditResult(), Constant.ROLE_CODE_DEPT_LEADER);
        dynamicTemp.setNodeCode(nodeCode);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(nodeCode));
        chiefWorkOrderDynamicManager.save(dynamicTemp);
        ChiefWorkOrderItem workOrderTemp = chiefWorkOrderItemManager.getOne(Wraps.<ChiefWorkOrderItem>lbQ().eq(ChiefWorkOrderItem::getId, auditVO.getOrderNo()));
        superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate()
                .set(ChiefWorkOrderTask::getCurrentNodeCode, nodeCode)
                .set(!Constant.SETTLE_CONDITION_ALL.equals(workOrderTemp.getSettleCondition()), ChiefWorkOrderTask::getLevel, Constant.TASK_LEVEL_0)
                .eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(ChiefWorkOrderTask::getId, auditVO.getId()));
        List<ChiefWorkOrderTask> taskTempList = superManager.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).eq(ChiefWorkOrderTask::getOrderNo, auditVO.getOrderNo()));
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
                List<ChiefWorkOrderTask> taskList = taskTempList.stream().filter(item -> !Objects.equals(Constant.NODE_CODE_BASIC_BACK_LEADER_APPROVE, item.getCurrentNodeCode())).toList();
                taskList.forEach(t -> t.setLevel(Constant.TASK_LEVEL_1));
                superManager.updateBatchById(taskList);
            }
            //如果是 结案12.1 通过，其他task不存在15.1，将其他的task level 置1；存在则本条task level置1
            if (Constant.NODE_CODE_BASIC_FINAL_LEADER_APPROVE.equals(nodeCode)) {
                List<ChiefWorkOrderTask> taskList = taskTempList.stream().filter(item -> Objects.equals(Constant.NODE_CODE_BASIC_BACK_LEADER_APPROVE, item.getCurrentNodeCode())).toList();
                if (!CollectionUtils.isEmpty(taskList)) {
                    superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate()
                            .set(ChiefWorkOrderTask::getLevel, Constant.TASK_LEVEL_1)
                            .eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID)
                            .eq(ChiefWorkOrderTask::getId, auditVO.getId()));
                } else {
                    List<ChiefWorkOrderTask> list = taskTempList.stream().filter(item -> !Objects.equals(Constant.NODE_CODE_BASIC_FINAL_LEADER_APPROVE, item.getCurrentNodeCode())).toList();
                    list.forEach(t -> t.setLevel(Constant.TASK_LEVEL_1));
                    superManager.updateBatchById(list);
                }
            }
            //如果是 结案12.2 不通过,并且是任意结案 本条task level置1
            if (Constant.NODE_CODE_BASIC_FINAL_LEADER_REJECT.equals(nodeCode) && taskTempList.size()>1) {
                superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate()
                        .set(ChiefWorkOrderTask::getLevel, Constant.TASK_LEVEL_1)
                        .eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID)
                        .eq(ChiefWorkOrderTask::getId, auditVO.getId()));
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean townBasicBackChiefWorkOrder(NormalWorkOrderTaskActionVO backVO) {
        //所有task node 变13.3, level都变0
        //新增 镇级退回的办理动态
        ChiefWorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(backVO, ChiefWorkOrderDynamic.class, "id", "createdTime", "updatedTime");
        dynamicTemp.setOrderNo(backVO.getOrderNo());
        dynamicTemp.setTaskId(backVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_BASIC_BACK);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_BASIC_BACK));
        chiefWorkOrderDynamicManager.save(dynamicTemp);
        //updateChiefWorkOrderStatus(backVO.getOrderNo());
        boolean result =  superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate()
                .set(ChiefWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_TOWN_BASIC_BACK)
                .set(ChiefWorkOrderTask::getLevel, Constant.TASK_LEVEL_0)
                .eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(ChiefWorkOrderTask::getOrderNo, backVO.getOrderNo()));
        if(result){
            updateChiefWorkOrderStatus(backVO.getOrderNo());

        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean townBasicFinishChiefWorkOrder(NormalWorkOrderTaskActionVO finishVO) {
        //所有task node 变13.1, level都变0
        //新增 镇级退回的办理动态
        ChiefWorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(finishVO, ChiefWorkOrderDynamic.class, "id", "createdTime", "updatedTime");
        dynamicTemp.setOrderNo(finishVO.getOrderNo());
        dynamicTemp.setTaskId(finishVO.getId());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_BASIC_FINAL);
        dynamicTemp.setProcessType(Constant.PROCESS_TYPE_MAP.get(Constant.NODE_CODE_TOWN_BASIC_FINAL));
        //更改normal_work_order 工单分类id 工单分类名称
        ChiefWorkOrderItem workOrderTemp = chiefWorkOrderItemManager.getOne(Wraps.<ChiefWorkOrderItem>lbQ().eq(ChiefWorkOrderItem::getId, finishVO.getOrderNo()));
        workOrderTemp.setOrderCategoryId(finishVO.getOrderCategoryId());
        workOrderTemp.setOrderCategoryName(finishVO.getOrderCategoryName());
        chiefWorkOrderItemManager.updateById(workOrderTemp);
        chiefWorkOrderDynamicManager.save(dynamicTemp);
        //updateChiefWorkOrderStatus(finishVO.getOrderNo());
        boolean result =  superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate()
                .set(ChiefWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_TOWN_BASIC_FINAL)
                .set(ChiefWorkOrderTask::getLevel, Constant.TASK_LEVEL_0)
                .eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID)
                .eq(ChiefWorkOrderTask::getOrderNo, finishVO.getOrderNo()));
        if(result){
            updateChiefWorkOrderStatus(finishVO.getOrderNo());

        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean revokeChiefWorkOrder(NormalWorkOrderTaskActionVO revokeVO) {
        List<ChiefWorkOrderTask> taskTempList = superManager.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).eq(ChiefWorkOrderTask::getOrderNo, revokeVO.getOrderNo()));
        ArgumentAssert.notEmpty(taskTempList, "工单编号有误");
        List<String> employeeIdList = Lists.newArrayList();
        String titleTemplate = "工单【%s】已撤回";
        List<BaseEmployeeResultVO> employeeList = baseEmployeeService.getEmployeeIdByRoleCodeAndOrgId(Arrays.asList(Constant.ROLE_CODE_DEPT_DIRECTOR, Constant.ROLE_CODE_DEPT_SPECIALIST), taskTempList.stream().map(ChiefWorkOrderTask::getLeadUnitId).collect(Collectors.toList()));
        if (!CollectionUtils.isEmpty(employeeList))
            employeeIdList.addAll(employeeList.stream().map(e -> String.valueOf(e.getId())).toList());
        Set<String> leaderEmployeeIdSet = taskTempList.stream().map(ChiefWorkOrderTask::getLeadEmployeeId).filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(leaderEmployeeIdSet))
            employeeIdList.addAll(leaderEmployeeIdSet);
        ChiefWorkOrderItem workOrderTemp = chiefWorkOrderItemManager.getOne(Wraps.<ChiefWorkOrderItem>lbQ().eq(ChiefWorkOrderItem::getId, revokeVO.getOrderNo()));
        ExtendMsgPublishVO data = new ExtendMsgPublishVO();
        data.setTitle(String.format(titleTemplate, workOrderTemp.getTitle()));
        data.setContent(workOrderTemp.getId().toString());
        data.setRemindMode("03");
        data.setRecipientList(employeeIdList);
        msgBiz.publish(data, new SysUser());
        superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate()
                .set(ChiefWorkOrderTask::getValid, Constant.TASK_INVALID)
                .eq(ChiefWorkOrderTask::getOrderNo, revokeVO.getOrderNo()));
        ChiefWorkOrderTask taskTemp = new ChiefWorkOrderTask();
        taskTemp.setOrderNo(revokeVO.getOrderNo());
        taskTemp.setCurrentNodeCode(Constant.NODE_CODE_TOWN_SIGN);
        superManager.save(taskTemp);
        //存入办理动态
        ChiefWorkOrderDynamic dynamicTemp = BeanUtil.toBean(revokeVO, ChiefWorkOrderDynamic.class);
        dynamicTemp.setOrderNo(revokeVO.getOrderNo());
        dynamicTemp.setNodeCode(Constant.NODE_CODE_TOWN_SIGN);
        dynamicTemp.setProcessType("撤回");
        return chiefWorkOrderDynamicManager.save(dynamicTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean urgeChiefWorkOrder(NormalWorkOrderTaskActionVO urgeVO) {
        ChiefWorkOrderItem workOrderTemp = chiefWorkOrderItemManager.getOne(Wraps.<ChiefWorkOrderItem>lbQ().eq(ChiefWorkOrderItem::getId, urgeVO.getOrderNo()));
        List<ChiefWorkOrderTask> taskTempList = superManager.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).eq(ChiefWorkOrderTask::getOrderNo, urgeVO.getOrderNo()));
        ArgumentAssert.notEmpty(taskTempList, "工单编号有误");
        List<String> employeeIdList = Lists.newArrayList();
        String titleTemplate = "请及时处理工单【%s】";
        taskTempList.forEach(taskTemp -> {
            String roleCode = NoticeNodeCodeEnum.getRoleCode(taskTemp.getCurrentNodeCode());
            if (StringUtils.isNotBlank(roleCode)) {
                if(Constant.ROLE_CODE_DEPT_LEADER.equals(roleCode)) {
                    employeeIdList.add(taskTemp.getLeadEmployeeId().toString());
                }else {
                    List<BaseEmployeeResultVO> employeeList = baseEmployeeService.getEmployeeIdByRoleCodeAndOrgId(List.of(roleCode), List.of(taskTemp.getLeadUnitId()));
                    if (!CollectionUtils.isEmpty(employeeList)) employeeIdList.addAll(employeeList.stream().map(e -> String.valueOf(e.getId())).toList());

                }
            }
        });
        ExtendMsgPublishVO data = new ExtendMsgPublishVO();
        data.setTitle(String.format(titleTemplate, workOrderTemp.getTitle()));
        data.setContent(workOrderTemp.getId().toString());
        data.setRemindMode("01");
        data.setRecipientList(employeeIdList);
        return msgBiz.publish(data, new SysUser());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean againProcessingChiefWorkOrder(NormalWorkOrderTaskActionVO processingVO) {
        List<ChiefWorkOrderTask> taskTempList = superManager.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).eq(ChiefWorkOrderTask::getOrderNo, processingVO.getOrderNo()));
        ArgumentAssert.notNull(taskTempList, "工单编号有误");
        Set<String> leadUnitIdSet = taskTempList.stream().map(ChiefWorkOrderTask::getLeadUnitId).map(String::valueOf).collect(Collectors.toSet());
        String[] leadUnitIdArray = processingVO.getLeadUnitIds().split(",");
        List<ChiefWorkOrderTask> existBackTask = taskTempList.stream().filter(a -> !Arrays.stream(leadUnitIdArray).collect(Collectors.toSet()).contains(a.getLeadUnitId().toString()) && !"1".equals(a.getLeadUnitId().toString()) && Constant.NODE_CODE_BASIC_BACK_LEADER_APPROVE.equals(a.getCurrentNodeCode())).toList();
        List<ChiefWorkOrderTask> otherTask = taskTempList.stream().filter(a -> !Arrays.stream(leadUnitIdArray).collect(Collectors.toSet()).contains(a.getLeadUnitId().toString()) && !"1".equals(a.getLeadUnitId().toString())).toList();
        leadUnitIdSet.addAll(Arrays.stream(leadUnitIdArray).collect(Collectors.toSet()));
        List<ChiefWorkOrderTask> normalWorkOrderTaskList = Lists.newArrayList();
        List<String> list = new ArrayList<>(Arrays.stream(leadUnitIdArray).toList());
        list.add("1"); //unit_id=1的(全部结案的主task)
        ArgumentAssert.notEmpty(leadUnitIdArray, "主办单位不可为空");
        //将需要重新交办的task valid置0  包含unit_id=1的(全部结案的主task)
        superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate().set(ChiefWorkOrderTask::getValid, Constant.TASK_INVALID).eq(ChiefWorkOrderTask::getOrderNo, processingVO.getOrderNo()).in(ChiefWorkOrderTask::getLeadUnitId, list));
        for (String leadUnitId : leadUnitIdArray) {//新增task
            ChiefWorkOrderTask taskTemp = BeanUtil.copyProperties(processingVO, ChiefWorkOrderTask.class, "id", "createdTime", "updatedTime");
            taskTemp.setLeadUnitId(Long.valueOf(leadUnitId));
            taskTemp.setCurrentNodeCode(Constant.NODE_CODE_BASIC_SIGN);
            taskTemp.setLevel(leadUnitIdSet.size() > 1 ? Constant.TASK_LEVEL_1 : Constant.TASK_LEVEL_0);
            normalWorkOrderTaskList.add(taskTemp);
        }
        if (Constant.SETTLE_CONDITION_ALL.equals(processingVO.getSettleCondition())) {
            normalWorkOrderTaskList.forEach(t -> t.setLevel(Constant.TASK_LEVEL_1));
            //将其他task的level置1
            if (CollectionUtils.isNotEmpty(otherTask)) {
                superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate().set(ChiefWorkOrderTask::getLevel, Constant.TASK_LEVEL_1)
                        .in(ChiefWorkOrderTask::getId, otherTask.stream().map(ChiefWorkOrderTask::getId).collect(Collectors.toList())));
            }
            ChiefWorkOrderTask taskTemp = BeanUtil.copyProperties(processingVO, ChiefWorkOrderTask.class, "id", "createdTime", "updatedTime");
            taskTemp.setCurrentNodeCode(CollectionUtils.isEmpty(existBackTask) ? Constant.NODE_CODE_BASIC_SIGN : Constant.NODE_CODE_BASIC_BACK_LEADER_APPROVE);
            taskTemp.setLeadUnitId(1L);
            taskTemp.setLevel(Constant.TASK_LEVEL_0);
            normalWorkOrderTaskList.add(taskTemp);
        } else {
            normalWorkOrderTaskList.forEach(t -> t.setLevel(Constant.TASK_LEVEL_0));
            //如果存在没动的task是15.1的 15.1的task level置0
            if (CollectionUtils.isNotEmpty(existBackTask)) {
                superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate().set(ChiefWorkOrderTask::getLevel, Constant.TASK_LEVEL_0)
                        .in(ChiefWorkOrderTask::getId, existBackTask.stream().map(ChiefWorkOrderTask::getId).collect(Collectors.toList()))
                        .eq(ChiefWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_BASIC_BACK_LEADER_APPROVE));
                normalWorkOrderTaskList.forEach(t -> t.setLevel(Constant.TASK_LEVEL_1));
            } else {//如果不存在没动的task是15.1的 12.1的task level置0
                if (CollectionUtils.isNotEmpty(otherTask)) {
                    boolean updated = superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate().set(ChiefWorkOrderTask::getLevel, Constant.TASK_LEVEL_0)
                            .in(ChiefWorkOrderTask::getId, otherTask.stream().map(ChiefWorkOrderTask::getId).collect(Collectors.toList()))
                            .eq(ChiefWorkOrderTask::getCurrentNodeCode, Constant.NODE_CODE_BASIC_FINAL_LEADER_APPROVE));
                    if (!updated)
                        superManager.update(new ChiefWorkOrderTask(), Wrappers.<ChiefWorkOrderTask>lambdaUpdate().set(ChiefWorkOrderTask::getLevel, Constant.TASK_LEVEL_0)
                                .in(ChiefWorkOrderTask::getId, otherTask.stream().map(ChiefWorkOrderTask::getId).collect(Collectors.toList())));
                    normalWorkOrderTaskList.forEach(t -> t.setLevel(Constant.TASK_LEVEL_1));
                }
            }
        }
        //新增 交办的办理动态
        ChiefWorkOrderDynamic dynamicTemp = BeanUtil.copyProperties(processingVO, ChiefWorkOrderDynamic.class, "id", "createdTime", "updatedTime");
        dynamicTemp.setNodeCode(Constant.NODE_CODE_BASIC_SIGN);
        dynamicTemp.setProcessType("交办");
        //更改normal_work_order 工单分类id 工单分类名称 结案条件 是否允许退回 允许退回时间 允许批示
        ChiefWorkOrderItem workOrderTemp = chiefWorkOrderItemManager.getOne(Wraps.<ChiefWorkOrderItem>lbQ().eq(ChiefWorkOrderItem::getId, processingVO.getOrderNo()));
        workOrderTemp.setOrderCategoryId(processingVO.getOrderCategoryId());
        workOrderTemp.setOrderCategoryName(processingVO.getOrderCategoryName());
        workOrderTemp.setSettleCondition(processingVO.getSettleCondition());
        workOrderTemp.setAllowBack(processingVO.getAllowBack());
        workOrderTemp.setAllowBackTime(processingVO.getAllowBackTime());
        workOrderTemp.setAllowComment(processingVO.getAllowComment());
        workOrderTemp.setCommentLeaderId(processingVO.getCommentLeaderId());
        superManager.saveOrUpdateBatch(normalWorkOrderTaskList);
        chiefWorkOrderDynamicManager.save(dynamicTemp);
        return chiefWorkOrderItemManager.updateById(workOrderTemp);
    }

    private void updateTownTaskNodeCode(List<ChiefWorkOrderTask> taskTempList, String nodeCode) {
        long count = taskTempList.stream().filter(item -> nodeCode.equals(item.getCurrentNodeCode())).count();
        List<ChiefWorkOrderTask> townTaskList = taskTempList.stream().filter(item -> Objects.equals(1L, item.getLeadUnitId())).toList();
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

    private void updateChiefWorkOrderStatus(String orderNo) {
        ChiefWorkOrderItem chiefWorkOrderItem = chiefWorkOrderItemManager.getById(Long.valueOf(orderNo));
        String batchNo = chiefWorkOrderItem.getBatchNo();
        List<String> orderNoList = chiefWorkOrderItemManager.list(Wraps.<ChiefWorkOrderItem>lbQ()
                .eq(ChiefWorkOrderItem::getBatchNo, batchNo)).stream().map(item -> String.valueOf(item.getId())).toList();
        ChiefWorkOrderItemPageQuery chiefWorkOrderItemPageQuery = new ChiefWorkOrderItemPageQuery();
        chiefWorkOrderItemPageQuery.setOrderNoList(orderNoList);
        Integer count = chiefWorkOrderItemManager.selectCountResultVO(chiefWorkOrderItemPageQuery);
        if (count == orderNoList.size()) {
            chiefWorkOrderManager.update(Wrappers.<ChiefWorkOrder>lambdaUpdate()
                    .eq(ChiefWorkOrder::getBatchNo, batchNo)
                    .set(ChiefWorkOrder::getStatus, Constant.CHIEF_ORDER_FINISH));
        }

    }
}
