package top.tangyh.lamp.base.controller;

import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.lamp.Constant;
import top.tangyh.lamp.base.service.ChiefWorkOrderItemService;
import top.tangyh.lamp.base.service.NormalWorkOrderService;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderRankingResultVO;
import top.tangyh.lamp.base.vo.result.SignCategoryIsNullNormalWorkOrderResultVO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/workOrderStatistic")
@Tag(name = "工单统计")
public class StatisticController {

    private final NormalWorkOrderService normalWorkOrderService;
    private final ChiefWorkOrderItemService chiefWorkOrderItemService;

    @GetMapping("/getWorkOrderStatistic")
    @Operation(summary = "统计数据", description = "统计数据")
    public R<Map<String, Object>> getWorkOrderStatistic(@RequestParam String roleCode, @RequestParam(required = false) String leadUnitId, @RequestParam(required = false) String leadEmployeeId) {
        Map<String, Object> responseMap = Maps.newLinkedHashMap();
        Map<String, Object> nomalCountMap = Maps.newLinkedHashMap();
        nomalCountMap.put("待签收", normalWorkOrderService.getWorkOrderCount("待签收", roleCode, leadUnitId, leadEmployeeId));
        nomalCountMap.put("处办中", normalWorkOrderService.getWorkOrderCount("处办中", roleCode, leadUnitId, leadEmployeeId));
        nomalCountMap.put("下级已退回", normalWorkOrderService.getWorkOrderCount("下级已退回", roleCode, leadUnitId, leadEmployeeId));
        nomalCountMap.put("下派跟踪", normalWorkOrderService.getWorkOrderCount("下派跟踪", roleCode, leadUnitId, leadEmployeeId));
        nomalCountMap.put("结案待审", normalWorkOrderService.getWorkOrderCount("结案待审", roleCode, leadUnitId, leadEmployeeId));
        nomalCountMap.put("办结", normalWorkOrderService.getWorkOrderCount("办结", roleCode, leadUnitId, leadEmployeeId));
        nomalCountMap.put("已退回", normalWorkOrderService.getWorkOrderCount("已退回", roleCode, leadUnitId, leadEmployeeId));
        responseMap.put("常规工单数量", nomalCountMap);
        List<SignCategoryIsNullNormalWorkOrderResultVO> groupCategoryList = normalWorkOrderService.groupByCategoryWorkOrderCount(roleCode, leadUnitId, leadEmployeeId);
        Map<String, Long> groupCategoryMap = groupCategoryList.stream().collect(Collectors.toMap(
                SignCategoryIsNullNormalWorkOrderResultVO::getCategoryName,
                SignCategoryIsNullNormalWorkOrderResultVO::getTotal));
        Boolean flag = Constant.ROLE_CODE_TOWN_LEADER.equals(roleCode) || Constant.ROLE_CODE_TOWN_SPECIALIST.equals(roleCode);
        groupCategoryMap.put("签收未分类", flag ? normalWorkOrderService.signCategoryIsNull() : 0L);
        responseMap.put("常规工单概览", groupCategoryMap);
        if (!flag) return R.success(responseMap);
        List<NormalWorkOrderRankingResultVO> rankingList = normalWorkOrderService.getRanking();
        responseMap.put("常规工单前10排名", rankingList.stream().limit(10).collect(Collectors.toList()));
        responseMap.put("常规工单全部排名", rankingList);
        return R.success(responseMap);
    }

    @GetMapping("/getChiefWorkOrderStatistic")
    @Operation(summary = "督办工单统计数据", description = "督办工单统计数据")
    public R<Map<String, Object>> getChiefWorkOrderStatistic(@RequestParam String roleCode, @RequestParam(required = false) String leadUnitId, @RequestParam(required = false) String leadEmployeeId) {
        Map<String, Object> responseMap = Maps.newLinkedHashMap();
        Map<String, Object> chiefCountMap = Maps.newLinkedHashMap();
        chiefCountMap.put("处办中", chiefWorkOrderItemService.getWorkOrderCount("处办中", roleCode, leadUnitId,leadEmployeeId));
        chiefCountMap.put("下级已退回", chiefWorkOrderItemService.getWorkOrderCount("下级已退回", roleCode, leadUnitId,leadEmployeeId));
        chiefCountMap.put("下派跟踪", chiefWorkOrderItemService.getWorkOrderCount("下派跟踪", roleCode, leadUnitId,leadEmployeeId));
        chiefCountMap.put("结案待审", chiefWorkOrderItemService.getWorkOrderCount("结案待审", roleCode, leadUnitId,leadEmployeeId));
        chiefCountMap.put("办结", chiefWorkOrderItemService.getWorkOrderCount("办结", roleCode, leadUnitId,leadEmployeeId));
        chiefCountMap.put("已退回", chiefWorkOrderItemService.getWorkOrderCount("已退回", roleCode, leadUnitId,leadEmployeeId));
        responseMap.put("督办工单数量", chiefCountMap);
        List<SignCategoryIsNullNormalWorkOrderResultVO> groupCategoryList = chiefWorkOrderItemService.groupByCategoryWorkOrderCount(roleCode, leadUnitId,leadEmployeeId);
        Map<String, Long> groupCategoryMap = groupCategoryList.stream().collect(Collectors.toMap(
                SignCategoryIsNullNormalWorkOrderResultVO::getCategoryName,
                SignCategoryIsNullNormalWorkOrderResultVO::getTotal));
        Boolean flag = Constant.ROLE_CODE_TOWN_LEADER.equals(roleCode) || Constant.ROLE_CODE_TOWN_SPECIALIST.equals(roleCode);
        groupCategoryMap.put("导入未分类", flag ? chiefWorkOrderItemService.signCategoryIsNull() : 0L);
        responseMap.put("督办工单概览", groupCategoryMap);
        if (!flag) return R.success(responseMap);
        List<NormalWorkOrderRankingResultVO> rankingList = chiefWorkOrderItemService.getRanking();
        responseMap.put("督办工单前10排名", rankingList.stream().limit(10).collect(Collectors.toList()));
        responseMap.put("督办工单全部排名", rankingList);
        return R.success(responseMap);
    }
}
