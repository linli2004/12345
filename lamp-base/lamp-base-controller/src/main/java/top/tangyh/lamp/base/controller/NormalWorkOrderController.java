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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
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
        } else {
            workOrderTaskList = normalWorkOrderTaskService.list(Wraps.<NormalWorkOrderTask>lbQ().eq(NormalWorkOrderTask::getLeadUnitId, params.getModel().getLeadUnitId()).eq(NormalWorkOrderTask::getValid, Constant.TASK_VALID).in(NormalWorkOrderTask::getOrderNo, orderNoList));
        }
        if (CollectionUtils.isEmpty(workOrderTaskList)) return R.success(page);
        List<NormalWorkOrderTaskResultVO> taskResultVOList = BeanUtil.copyToList(workOrderTaskList, NormalWorkOrderTaskResultVO.class);
        if (echoService != null) {
            echoService.action(taskResultVOList);
        }
        if (!Constant.ROLE_CODE_TOWN_SPECIALIST.equals(params.getModel().getRoleCode()) &&  !Constant.ROLE_CODE_TOWN_LEADER.equals(params.getModel().getRoleCode())) {
            List<BaseEmployeeResultVO> leader = baseEmployeeService.getEmployeeIdByRoleCodeAndOrgId(List.of(Constant.ROLE_CODE_DEPT_LEADER), List.of(Long.valueOf(params.getModel().getLeadUnitId())));
            List<BaseEmployeeResultVO> director = baseEmployeeService.getEmployeeIdByRoleCodeAndOrgId(List.of(Constant.ROLE_CODE_DEPT_DIRECTOR), List.of(Long.valueOf(params.getModel().getLeadUnitId())));
            if (!CollectionUtils.isEmpty(leader))
                taskResultVOList.forEach(t -> t.setDeptLeader(leader.get(0).getRealName()));
            if (!CollectionUtils.isEmpty(director))
                taskResultVOList.forEach(t -> t.setDeptDirector(director.get(0).getRealName()));
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
        page.getRecords().forEach(t -> t.setWorkOrderTaskList(taskMap.get(t.getOrderNo())));
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


