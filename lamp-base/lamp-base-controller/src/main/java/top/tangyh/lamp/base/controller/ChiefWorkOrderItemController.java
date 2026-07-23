package top.tangyh.lamp.base.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.Constant;
import top.tangyh.lamp.base.entity.ChiefWorkOrderItem;
import top.tangyh.lamp.base.entity.ChiefWorkOrderTask;
import top.tangyh.lamp.base.entity.NormalWorkOrderTask;
import top.tangyh.lamp.base.entity.user.BaseEmployee;
import top.tangyh.lamp.base.service.ChiefWorkOrderItemService;
import top.tangyh.lamp.base.service.ChiefWorkOrderTaskService;
import top.tangyh.lamp.base.service.user.BaseEmployeeService;
import top.tangyh.lamp.base.vo.query.ChiefWorkOrderItemPageQuery;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderItemResultVO;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderTaskResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderTaskResultVO;
import top.tangyh.lamp.base.vo.result.user.BaseEmployeeResultVO;
import top.tangyh.lamp.base.vo.save.ChiefWorkOrderItemSaveVO;
import top.tangyh.lamp.base.vo.update.ChiefWorkOrderItemUpdateVO;
import top.tangyh.lamp.msg.entity.ExtendMsg;
import top.tangyh.lamp.msg.service.ExtendMsgService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * 督办工单详情
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/chiefWorkOrderItem")
@Tag(name = "督办工单详情")
public class ChiefWorkOrderItemController extends SuperController<ChiefWorkOrderItemService, Long, ChiefWorkOrderItem
        , ChiefWorkOrderItemSaveVO, ChiefWorkOrderItemUpdateVO, ChiefWorkOrderItemPageQuery, ChiefWorkOrderItemResultVO> {
    private final EchoService echoService;
    private final ChiefWorkOrderTaskService chiefWorkOrderTaskService;
    private final BaseEmployeeService baseEmployeeService;
    private final ExtendMsgService extendMsgService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Operation(summary = "查询统计 督办工单", description = "查询统计 督办工单")
    @PostMapping(path = "/pageChiefStatistic")
    @WebLog("查询统计 督办工单")
    public R<IPage<ChiefWorkOrderItemResultVO>> pageStatistic(@RequestBody @Validated PageParams<ChiefWorkOrderItemPageQuery> params) {
        IPage<ChiefWorkOrderItemResultVO> page = superService.selectOrderAllConditions(params);
        List<String> orderNoList = page.getRecords().stream().filter(item -> "办结".equals(item.getStatus())).map(item -> String.valueOf(item.getId())).toList();
        List<ChiefWorkOrderTask> workOrderTaskList = chiefWorkOrderTaskService.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).in(ChiefWorkOrderTask::getOrderNo, orderNoList));
        if (CollectionUtils.isEmpty(workOrderTaskList)) return R.success(page);
        List<ChiefWorkOrderTaskResultVO> taskResultVOList = BeanUtil.copyToList(workOrderTaskList, ChiefWorkOrderTaskResultVO.class);
        if (echoService != null) {
            echoService.action(taskResultVOList);
        }
        Map<String, List<ChiefWorkOrderTaskResultVO>> taskMap = taskResultVOList.stream()
                .collect(Collectors.groupingBy(
                        ChiefWorkOrderTaskResultVO::getOrderNo,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        page.getRecords().forEach(t -> t.setWorkOrderTaskList(taskMap.get(String.valueOf(t.getId()))));
        
        params.getModel().setOrderNoList(orderNoList);
        params.getModel().setDisplayStatus("办结");

        if (!CollectionUtils.isEmpty(orderNoList)) {
            superService.getFinishOrBackContentJson(page.getRecords(), params.getModel());
        }

        return R.success(page);
    }

    @PostMapping("/itemPage")
    @Operation(summary = "导入详情", description = "导入详情")
    public R<IPage<ChiefWorkOrderItemResultVO>> itemPage(@RequestBody @Validated PageParams<ChiefWorkOrderItemPageQuery> params) {
        return super.page(params);
    }

    @Override
    public R<IPage<ChiefWorkOrderItemResultVO>> page(@RequestBody @Validated PageParams<ChiefWorkOrderItemPageQuery> params) {
        IPage<ChiefWorkOrderItemResultVO> pageResultVO = superService.findPageResultVO(params);
        if (null != params.getModel().getCoOrccType()) return R.success(pageResultVO);
        List<String> orderNoList = pageResultVO.getRecords().stream().map(item -> String.valueOf(item.getId())).toList();
        List<ChiefWorkOrderTask> workOrderTaskList = null;
        if (Constant.ROLE_CODE_TOWN_SPECIALIST.equals(params.getModel().getRoleCode()) || Constant.ROLE_CODE_TOWN_LEADER.equals(params.getModel().getRoleCode())) {
            workOrderTaskList = chiefWorkOrderTaskService.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).in(ChiefWorkOrderTask::getOrderNo, orderNoList));
        } else if (Constant.ROLE_CODE_DEPT_LEADER.equals(params.getModel().getRoleCode())) {
            if("处办中".equals(params.getModel().getDisplayStatus()) || "下级已退回".equals(params.getModel().getDisplayStatus()) || "结案待审".equals(params.getModel().getDisplayStatus())) {
                workOrderTaskList = chiefWorkOrderTaskService.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getLeadEmployeeId, params.getModel().getLeadEmployeeId()).eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).last("and current_node_code IN ( SELECT t1.node_code FROM role_status_mapping t1 WHERE t1.display_status = '"+params.getModel().getDisplayStatus()+"' AND t1.role_code = '2')").in(ChiefWorkOrderTask::getOrderNo, orderNoList));
            } else {
                workOrderTaskList = chiefWorkOrderTaskService.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getLeadEmployeeId, params.getModel().getLeadEmployeeId()).eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).in(ChiefWorkOrderTask::getOrderNo, orderNoList));
            }

        } else {
            workOrderTaskList = chiefWorkOrderTaskService.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getLeadUnitId, params.getModel().getLeadUnitId()).eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).in(ChiefWorkOrderTask::getOrderNo, orderNoList));
        }
        if (CollectionUtils.isEmpty(workOrderTaskList)) return R.success(pageResultVO);
        List<ChiefWorkOrderTaskResultVO> taskResultVOList = BeanUtil.copyToList(workOrderTaskList, ChiefWorkOrderTaskResultVO.class);
        if (echoService != null) {
            echoService.action(taskResultVOList);
        }
        if (!Constant.ROLE_CODE_TOWN_SPECIALIST.equals(params.getModel().getRoleCode()) && !Constant.ROLE_CODE_TOWN_LEADER.equals(params.getModel().getRoleCode())) {
            Set<Long> unitIdSet = taskResultVOList.stream().map(ChiefWorkOrderTaskResultVO::getLeadUnitId).collect(Collectors.toSet());
            List<BaseEmployeeResultVO> directorEmployeeList = baseEmployeeService.getEmployeeIdByRoleCodeAndOrgId(List.of(Constant.ROLE_CODE_DEPT_DIRECTOR), new ArrayList<>(unitIdSet));
            if (!CollectionUtils.isEmpty(directorEmployeeList)) {
                Map<Long, String> directorEmployeeMap = directorEmployeeList.stream().collect(Collectors.toMap(BaseEmployeeResultVO::getDirectorUnitId, BaseEmployeeResultVO::getRealName));
                taskResultVOList.forEach(t -> t.setDeptDirector(directorEmployeeMap.get(t.getLeadUnitId())));
            }
            Set<Long> leaderEmployeeIdSet = taskResultVOList.stream().map(ChiefWorkOrderTaskResultVO::getLeadEmployeeId).filter(Objects::nonNull).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(leaderEmployeeIdSet)) {
                List<BaseEmployee> leaderEmployeeList = baseEmployeeService.findByIds(leaderEmployeeIdSet, null);
                if (!CollectionUtils.isEmpty(leaderEmployeeList)) {
                    Map<Long, String> leaderEmployeeMap = leaderEmployeeList.stream().collect(Collectors.toMap(BaseEmployee::getId, BaseEmployee::getRealName));
                    taskResultVOList.forEach(t -> {
                        if (null != t.getLeadEmployeeId() && leaderEmployeeMap.containsKey(t.getLeadEmployeeId()))
                            t.setDeptLeader(leaderEmployeeMap.get(t.getLeadEmployeeId()));
                    });
                }
            }
        }
        taskResultVOList.forEach(t -> t.setCurrentNodeName(Constant.SUB_WORK_ORDER_TYPE_MAP.get(t.getCurrentNodeCode())));
        Map<String, List<ChiefWorkOrderTaskResultVO>> taskMap = taskResultVOList.stream()
                .collect(Collectors.groupingBy(
                        ChiefWorkOrderTaskResultVO::getOrderNo,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        List<ExtendMsg> urgeList = extendMsgService.list(Wraps.<ExtendMsg>lbQ().eq(ExtendMsg::getStatus, "SUCCESS").eq(ExtendMsg::getRemindMode, "01").in(ExtendMsg::getContent, orderNoList));
        Map<String, List<ExtendMsg>> urgeMap = urgeList.stream()
                .collect(Collectors.groupingBy(
                        ExtendMsg::getContent,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        pageResultVO.getRecords().forEach(t -> {
            t.setWorkOrderTaskList(taskMap.get(t.getId().toString()));
            t.setUrgeList(urgeMap.get(t.getId().toString()));
        });
        return R.success(pageResultVO);
    }

    @PostMapping("/list")
    public R<List<ChiefWorkOrderItemResultVO>> list(@RequestBody ChiefWorkOrderItemPageQuery model) {
        return R.success(superService.selectListResultVO(model));
    }

    @PostMapping("/export")
    public void export(@RequestBody List<String> orderNoList, HttpServletResponse response, String status) {
        superService.exportTaskZip(orderNoList, response, status);
    }

    @Operation(summary = "督办工单待批示", description = "督办工单待批示")
    @PostMapping(path = "/notComment")
    @WebLog("督办工单待批示")
    public R<IPage<ChiefWorkOrderItemResultVO>> notCommentPage(@RequestBody @Validated PageParams<ChiefWorkOrderItemPageQuery> params) {
        IPage<ChiefWorkOrderItemResultVO> notCommentPageResultVO = superService.findNotCommentPageResultVO(params);
        List<String> orderNoList = notCommentPageResultVO.getRecords().stream().map(item -> String.valueOf(item.getId())).toList();
        List<ChiefWorkOrderTask> workOrderTaskList = chiefWorkOrderTaskService.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).in(ChiefWorkOrderTask::getOrderNo, orderNoList));
        if (CollectionUtils.isEmpty(workOrderTaskList)) return R.success(notCommentPageResultVO);
        List<ChiefWorkOrderTaskResultVO> taskResultVOList = BeanUtil.copyToList(workOrderTaskList, ChiefWorkOrderTaskResultVO.class);
        if (echoService != null) {
            echoService.action(taskResultVOList);
        }
        Map<String, List<ChiefWorkOrderTaskResultVO>> taskMap = taskResultVOList.stream()
                .collect(Collectors.groupingBy(
                        ChiefWorkOrderTaskResultVO::getOrderNo,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        notCommentPageResultVO.getRecords().forEach(t -> t.setWorkOrderTaskList(taskMap.get(t.getId().toString())));
        return R.success(notCommentPageResultVO);
    }

    @Operation(summary = "督办工单已批示", description = "督办工单已批示")
    @PostMapping(path = "/commented")
    @WebLog("督办工单已批示")
    public R<IPage<ChiefWorkOrderItemResultVO>> commentedPage(@RequestBody @Validated PageParams<ChiefWorkOrderItemPageQuery> params) {
        IPage<ChiefWorkOrderItemResultVO> commentedPageResultVO = superService.findCommentedPageResultVO(params);
        List<String> orderNoList = commentedPageResultVO.getRecords().stream().map(item -> String.valueOf(item.getId())).toList();
        List<ChiefWorkOrderTask> workOrderTaskList = chiefWorkOrderTaskService.list(Wraps.<ChiefWorkOrderTask>lbQ().eq(ChiefWorkOrderTask::getValid, Constant.TASK_VALID).in(ChiefWorkOrderTask::getOrderNo, orderNoList));
        if (CollectionUtils.isEmpty(workOrderTaskList)) return R.success(commentedPageResultVO);
        List<ChiefWorkOrderTaskResultVO> taskResultVOList = BeanUtil.copyToList(workOrderTaskList, ChiefWorkOrderTaskResultVO.class);
        if (echoService != null) {
            echoService.action(taskResultVOList);
        }
        Map<String, List<ChiefWorkOrderTaskResultVO>> taskMap = taskResultVOList.stream()
                .collect(Collectors.groupingBy(
                        ChiefWorkOrderTaskResultVO::getOrderNo,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        commentedPageResultVO.getRecords().forEach(t -> t.setWorkOrderTaskList(taskMap.get(t.getId().toString())));
        return R.success(commentedPageResultVO);
    }
}
