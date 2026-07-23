package top.tangyh.lamp.base.controller;

import cn.hutool.core.bean.BeanUtil;
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
import org.springframework.web.multipart.MultipartFile;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.Constant;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.entity.NormalWorkOrderTask;
import top.tangyh.lamp.base.entity.user.BaseEmployee;
import top.tangyh.lamp.base.service.NormalWorkOrderService;
import top.tangyh.lamp.base.service.NormalWorkOrderTaskService;
import top.tangyh.lamp.base.service.user.BaseEmployeeService;
import top.tangyh.lamp.base.vo.query.NormalWorkOrderPageQuery;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderResultVO;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderTaskResultVO;
import top.tangyh.lamp.base.vo.result.user.BaseEmployeeResultVO;
import top.tangyh.lamp.base.vo.save.NormalWorkOrderSaveVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderUpdateVO;
import top.tangyh.lamp.msg.entity.ExtendMsg;
import top.tangyh.lamp.msg.service.ExtendMsgService;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

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
    private final BaseEmployeeService baseEmployeeService;
    private final ExtendMsgService extendMsgService;
    /**
     * 普通工单导入
     *
     * @param actionVO excel工单文件
     */
    @Operation(summary = "普通工单导入", description = "普通工单导入")
    @PostMapping(path = "/import", consumes = "multipart/form-data")
    @WebLog("普通工单导入")
    public R importNormalWorkOrder(@RequestPart("file") MultipartFile file, NormalWorkOrderTaskActionVO actionVO) throws IOException {
        if (file.isEmpty()) {
            return R.fail("导入的文件为空");
        }
        List<String> errorOrderNoList = Lists.newArrayList();
        try (InputStream inputStream = file.getInputStream()) {
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
        if (null != params.getModel().getCoOrccType()) return R.success(page);
        List<String> orderNoList = page.getRecords().stream().map(NormalWorkOrderResultVO::getOrderNo).toList();
        List<NormalWorkOrderTask> workOrderTaskList = null;
        if (Constant.ROLE_CODE_TOWN_SPECIALIST.equals(params.getModel().getRoleCode()) || Constant.ROLE_CODE_TOWN_LEADER.equals(params.getModel().getRoleCode())) {
            workOrderTaskList = normalWorkOrderTaskService.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).in(NormalWorkOrderTask::getOrderNo, orderNoList));
        } else if (Constant.ROLE_CODE_DEPT_LEADER.equals(params.getModel().getRoleCode())) {
            if("处办中".equals(params.getModel().getDisplayStatus()) || "下级已退回".equals(params.getModel().getDisplayStatus()) || "结案待审".equals(params.getModel().getDisplayStatus())) {
                workOrderTaskList = normalWorkOrderTaskService.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getLeadEmployeeId, params.getModel().getLeadEmployeeId()).eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).last("and current_node_code IN ( SELECT t1.node_code FROM role_status_mapping t1 WHERE t1.display_status = '"+params.getModel().getDisplayStatus()+"' AND t1.role_code = '2')").in(NormalWorkOrderTask::getOrderNo, orderNoList));
            } else {
                workOrderTaskList = normalWorkOrderTaskService.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getLeadEmployeeId, params.getModel().getLeadEmployeeId()).eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).in(NormalWorkOrderTask::getOrderNo, orderNoList));
            }

        } else {
            workOrderTaskList = normalWorkOrderTaskService.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getLeadUnitId, params.getModel().getLeadUnitId()).eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).in(NormalWorkOrderTask::getOrderNo, orderNoList));
        }
        if (CollectionUtils.isEmpty(workOrderTaskList)) return R.success(page);
        List<NormalWorkOrderTaskResultVO> taskResultVOList = BeanUtil.copyToList(workOrderTaskList, NormalWorkOrderTaskResultVO.class);
        if (echoService != null) {
            echoService.action(taskResultVOList);
        }
        if (!Constant.ROLE_CODE_TOWN_SPECIALIST.equals(params.getModel().getRoleCode()) &&  !Constant.ROLE_CODE_TOWN_LEADER.equals(params.getModel().getRoleCode())) {
            Set<Long> unitIdSet = taskResultVOList.stream().map(NormalWorkOrderTaskResultVO::getLeadUnitId).collect(Collectors.toSet());
            List<BaseEmployeeResultVO> directorEmployeeList = baseEmployeeService.getEmployeeIdByRoleCodeAndOrgId(List.of(Constant.ROLE_CODE_DEPT_DIRECTOR), new ArrayList<>(unitIdSet));
            if (!CollectionUtils.isEmpty(directorEmployeeList)) {
                Map<Long, String> directorEmployeeMap = directorEmployeeList.stream().collect(Collectors.toMap(BaseEmployeeResultVO::getDirectorUnitId, BaseEmployeeResultVO::getRealName));
                taskResultVOList.forEach(t -> t.setDeptDirector(directorEmployeeMap.get(t.getLeadUnitId())));
            }
            Set<Long> leaderEmployeeIdSet = taskResultVOList.stream().map(NormalWorkOrderTaskResultVO::getLeadEmployeeId).filter(Objects::nonNull).collect(Collectors.toSet());
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
        Map<String, List<NormalWorkOrderTaskResultVO>> taskMap = taskResultVOList.stream()
                .collect(Collectors.groupingBy(
                        NormalWorkOrderTaskResultVO::getOrderNo,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        params.getModel().setOrderNoList(orderNoList);
        superService.getFinishOrBackContentJson(page.getRecords(), params.getModel());
        List<ExtendMsg> urgeList = extendMsgService.list(Wraps.<ExtendMsg>lbQ().eq(ExtendMsg::getStatus, "SUCCESS").eq(ExtendMsg::getRemindMode, "01").in(ExtendMsg::getContent, orderNoList));
        Map<String, List<ExtendMsg>> urgeMap = urgeList.stream()
                .collect(Collectors.groupingBy(
                        ExtendMsg::getContent,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        page.getRecords().forEach(t -> {
            t.setWorkOrderTaskList(taskMap.get(t.getOrderNo()));
            t.setUrgeList(urgeMap.get(t.getOrderNo()));
        });
        return R.success(page);
    }


    /**
     * 普通工单导出压缩包
     * <p>
     * 导出包含工单Excel清单和每个工单详情Word文档的压缩包
     *
     * @param status   工单状态
     * @param response HTTP响应对象，用于写入ZIP流
     */
    @PostMapping("/exportTaskZip")
    @Operation(summary = "普通工单导出压缩包", description = "普通工单导出压缩包")
    public void exportTaskZip(@RequestBody List<String> orderNoList, String status, HttpServletResponse response) {
        superService.exportTaskZip(orderNoList, response, status);
    }

    @PostMapping("/testList")
    @Operation(summary = "测试list", description = "测试list")
    public R<List<NormalWorkOrderResultVO>> testLIst(@RequestBody NormalWorkOrderPageQuery model) {
        return R.success(superService.selectListResultVO(model));
    }

    @Operation(summary = "普通工单待批示", description = "普通工单待批示")
    @PostMapping(path = "/notComment")
    @WebLog("普通工单待批示")
    public R<IPage<NormalWorkOrderResultVO>> notCommentPage(@RequestBody @Validated PageParams<NormalWorkOrderPageQuery> params) {
        IPage<NormalWorkOrderResultVO> notCommentPageResultVO = superService.findNotCommentPageResultVO(params);
        List<String> orderNoList = notCommentPageResultVO.getRecords().stream().map(NormalWorkOrderResultVO::getOrderNo).toList();
        List<NormalWorkOrderTask> workOrderTaskList = normalWorkOrderTaskService.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).in(NormalWorkOrderTask::getOrderNo, orderNoList));
        if (CollectionUtils.isEmpty(workOrderTaskList)) return R.success(notCommentPageResultVO);
        List<NormalWorkOrderTaskResultVO> taskResultVOList = BeanUtil.copyToList(workOrderTaskList, NormalWorkOrderTaskResultVO.class);
        if (echoService != null) {
            echoService.action(taskResultVOList);
        }
        Map<String, List<NormalWorkOrderTaskResultVO>> taskMap = taskResultVOList.stream()
                .collect(Collectors.groupingBy(
                        NormalWorkOrderTaskResultVO::getOrderNo,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        notCommentPageResultVO.getRecords().forEach(t -> t.setWorkOrderTaskList(taskMap.get(t.getOrderNo())));
        return R.success(notCommentPageResultVO);
    }

    @Operation(summary = "普通工单已批示", description = "普通工单已批示")
    @PostMapping(path = "/commented")
    @WebLog("普通工单已批示")
    public R<IPage<NormalWorkOrderResultVO>> commentedPage(@RequestBody @Validated PageParams<NormalWorkOrderPageQuery> params) {
        IPage<NormalWorkOrderResultVO> commentedPageResultVO = superService.findCommentedPageResultVO(params);
        List<String> orderNoList = commentedPageResultVO.getRecords().stream().map(NormalWorkOrderResultVO::getOrderNo).toList();
        List<NormalWorkOrderTask> workOrderTaskList = normalWorkOrderTaskService.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).in(NormalWorkOrderTask::getOrderNo, orderNoList));
        if (CollectionUtils.isEmpty(workOrderTaskList)) return R.success(commentedPageResultVO);
        List<NormalWorkOrderTaskResultVO> taskResultVOList = BeanUtil.copyToList(workOrderTaskList, NormalWorkOrderTaskResultVO.class);
        if (echoService != null) {
            echoService.action(taskResultVOList);
        }
        Map<String, List<NormalWorkOrderTaskResultVO>> taskMap = taskResultVOList.stream()
                .collect(Collectors.groupingBy(
                        NormalWorkOrderTaskResultVO::getOrderNo,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        commentedPageResultVO.getRecords().forEach(t -> t.setWorkOrderTaskList(taskMap.get(t.getOrderNo())));
        return R.success(commentedPageResultVO);
    }

    /**
     * 查询统计 普通工单
     */
    @Operation(summary = "查询统计 普通工单", description = "查询统计 普通工单")
    @PostMapping(path = "/pageNormalStatistic")
    @WebLog("查询统计 普通工单")
    public R<IPage<NormalWorkOrderResultVO>> pageStatistic(@RequestBody @Validated PageParams<NormalWorkOrderPageQuery> params) {
        IPage<NormalWorkOrderResultVO> page = superService.selectOrderAllConditions(params);
        List<String> orderNoList = page.getRecords().stream().filter(item -> "办结".equals(item.getStatus())).map(NormalWorkOrderResultVO::getOrderNo).toList();
        List<NormalWorkOrderTask> workOrderTaskList = normalWorkOrderTaskService.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).in(NormalWorkOrderTask::getOrderNo, orderNoList));
        if (CollectionUtils.isEmpty(workOrderTaskList)) return R.success(page);
        List<NormalWorkOrderTaskResultVO> taskResultVOList = BeanUtil.copyToList(workOrderTaskList, NormalWorkOrderTaskResultVO.class);
        if (echoService != null) {
            echoService.action(taskResultVOList);
        }
        Map<String, List<NormalWorkOrderTaskResultVO>> taskMap = taskResultVOList.stream()
                .collect(Collectors.groupingBy(
                        NormalWorkOrderTaskResultVO::getOrderNo,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()
                ));
        page.getRecords().forEach(t -> t.setWorkOrderTaskList(taskMap.get(t.getOrderNo())));
        params.getModel().setOrderNoList(orderNoList);
        params.getModel().setDisplayStatus("办结");
        if (!CollectionUtils.isEmpty(orderNoList))
            superService.getFinishOrBackContentJson(page.getRecords(), params.getModel());
        return R.success(page);
    }
}


