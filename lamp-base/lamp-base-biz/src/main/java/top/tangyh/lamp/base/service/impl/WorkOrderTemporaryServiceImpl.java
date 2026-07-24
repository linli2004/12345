package top.tangyh.lamp.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.entity.WorkOrderDynamic;
import top.tangyh.lamp.base.entity.WorkOrderTemporary;
import top.tangyh.lamp.base.manager.NormalWorkOrderManager;
import top.tangyh.lamp.base.manager.WorkOrderDynamicManager;
import top.tangyh.lamp.base.manager.WorkOrderTemporaryManager;
import top.tangyh.lamp.base.service.WorkOrderTemporaryService;
import top.tangyh.lamp.base.vo.query.WorkOrderTemporaryPageQuery;
import top.tangyh.lamp.base.vo.result.WorkOrderTemporaryResultVO;
import top.tangyh.lamp.common.constant.DsConstant;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务实现类
 * 工单办理暂存
 * </p>
 *
 * @author lunar
 * @date 2026-03-12 11:50:36
 * @create [2026-03-12 11:50:36] [lunar] [代码生成器生成]
 */
@DS(DsConstant.BASE_TENANT)
@Slf4j
@RequiredArgsConstructor
@Service
public class WorkOrderTemporaryServiceImpl extends SuperServiceImpl<WorkOrderTemporaryManager, Long, WorkOrderTemporary> implements WorkOrderTemporaryService {
    private final NormalWorkOrderManager normalWorkOrderManager;
    private final WorkOrderDynamicManager workOrderDynamicManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <SaveVO> WorkOrderTemporary save(SaveVO saveVO) {
        WorkOrderTemporary workOrderTemporary = BeanUtil.copyProperties(saveVO, WorkOrderTemporary.class);
        superManager.saveOrUpdate(workOrderTemporary);
        return workOrderTemporary;
    }

    // BUGFIX-12345-004 START
    @Override
    public WorkOrderTemporaryResultVO buildFinishAuditTemporary(WorkOrderTemporaryPageQuery query) {
        if (query == null || StringUtils.isBlank(query.getOrderNo())
                || !"结案待审结案".equals(query.getNodeName())) {
            return null;
        }

        // 1. 查询主表
        List<NormalWorkOrder> orderList = normalWorkOrderManager.list(
                Wraps.<NormalWorkOrder>lbQ()
                        .eq(NormalWorkOrder::getOrderNo, query.getOrderNo())
                        .last("LIMIT 1")
        );
        if (CollectionUtils.isEmpty(orderList)) {
            return null;
        }
        NormalWorkOrder order = orderList.get(0);

        // 2. 查询最新交办动态（用于划定本轮时间边界）
        WorkOrderDynamic assignDynamic = findLatestAssignDynamic(query.getOrderNo());
        if (assignDynamic == null || assignDynamic.getCreatedTime() == null) {
            return null;
        }

        // 3. 查询交办时间之后的办结动态
        List<WorkOrderDynamic> finishDynamics = findCurrentRoundFinishDynamics(
                query.getOrderNo(), assignDynamic.getCreatedTime());
        if (CollectionUtils.isEmpty(finishDynamics)) {
            return null;
        }

        // 4. 按 taskId 去重，判断单派/多派
        Map<Long, WorkOrderDynamic> latestFinishByTaskId = groupLatestFinishByTaskId(finishDynamics);
        int sourceCount = latestFinishByTaskId.size();
        if (sourceCount != 1) {
            log.info("BUGFIX-12345-004 不生成回填，orderNo={}, finishSourceCount={}, taskIds={}",
                    query.getOrderNo(), sourceCount, latestFinishByTaskId.keySet());
            return null;
        }
        WorkOrderDynamic finishDynamic = latestFinishByTaskId.values().iterator().next();

        // 5. 白名单构造回填 JSON
        JSONObject content = buildFinishAuditContent(order, finishDynamic);

        // 6. 构造临时返回对象（不入库）
        WorkOrderTemporaryResultVO result = new WorkOrderTemporaryResultVO();
        result.setOrderNo(query.getOrderNo());
        result.setTaskIds(query.getTaskIds());
        result.setNodeName(query.getNodeName());
        result.setOperatorId(query.getOperatorId());
        result.setContentJson(content.toJSONString());

        log.info("BUGFIX-12345-004 已构造单派回填，orderNo={}, sourceTaskId={}, keys={}",
                query.getOrderNo(), finishDynamic.getTaskId(), content.keySet());
        return result;
    }

    private WorkOrderDynamic findLatestAssignDynamic(String orderNo) {
        List<WorkOrderDynamic> list = workOrderDynamicManager.list(
                Wraps.<WorkOrderDynamic>lbQ()
                        .eq(WorkOrderDynamic::getOrderNo, orderNo)
                        .eq(WorkOrderDynamic::getProcessType, "交办")
                        .orderByDesc(WorkOrderDynamic::getCreatedTime)
                        .last("LIMIT 1")
        );
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    private List<WorkOrderDynamic> findCurrentRoundFinishDynamics(
            String orderNo, java.time.LocalDateTime assignTime) {
        return workOrderDynamicManager.list(
                Wraps.<WorkOrderDynamic>lbQ()
                        .eq(WorkOrderDynamic::getOrderNo, orderNo)
                        .eq(WorkOrderDynamic::getProcessType, "办结")
                        .isNotNull(WorkOrderDynamic::getTaskId)
                        .ge(WorkOrderDynamic::getCreatedTime, assignTime)
                        .orderByDesc(WorkOrderDynamic::getCreatedTime)
        );
    }

    private Map<Long, WorkOrderDynamic> groupLatestFinishByTaskId(
            List<WorkOrderDynamic> finishDynamics) {
        Map<Long, WorkOrderDynamic> latestByTaskId = new LinkedHashMap<>();
        if (CollectionUtils.isEmpty(finishDynamics)) {
            return latestByTaskId;
        }
        for (WorkOrderDynamic dynamic : finishDynamics) {
            if (dynamic.getTaskId() != null) {
                // 查询已按 createdTime DESC 排序，首次出现的即为最新
                latestByTaskId.putIfAbsent(dynamic.getTaskId(), dynamic);
            }
        }
        return latestByTaskId;
    }

    private JSONObject buildFinishAuditContent(NormalWorkOrder order,
                                               WorkOrderDynamic finishDynamic) {
        JSONObject result = new JSONObject();
        JSONObject finishContent = parseContent(finishDynamic);

        // 1. 办结动态白名单
        copyAllowedFields(finishContent, result, true);

        // 2. 主表基础字段权威覆盖
        putIfPresent(result, "orderCategoryId", order.getOrderCategoryId());
        putIfPresent(result, "orderCategoryName", order.getOrderCategoryName());
        putIfPresent(result, "incidentLocation", order.getIncidentLocation());
        putIfPresent(result, "address", order.getAddress());

        // 3. 结案单位没有时，用办结动态的办理单位兜底
        if (!hasValue(result, "closingUnit")
                && StringUtils.isNotBlank(finishDynamic.getDeptName())) {
            result.put("closingUnit", finishDynamic.getDeptName());
        }

        return result;
    }

    private void copyAllowedFields(JSONObject source, JSONObject target, boolean override) {
        if (source == null) {
            return;
        }
        copyField(source, target, "contactCitizenFirst", override);
        copyField(source, target, "notifyCitizenFirst", override);
        copyField(source, target, "replyResult", override);
        copyField(source, target, "closingUnit", override);
        copyField(source, target, "replier", override);
        copyField(source, target, "replyTime", override);
        copyField(source, target, "isFinalReply", override);
        copyField(source, target, "isOnSite", override);
    }

    private void copyField(JSONObject source, JSONObject target, String key, boolean override) {
        if (!source.containsKey(key)) {
            return;
        }
        Object value = source.get(key);
        if (!hasValue(value)) {
            return;
        }
        if (!override && target.containsKey(key)) {
            return;
        }
        target.put(key, value);
    }

    private static boolean hasValue(JSONObject obj, String key) {
        return obj != null && obj.containsKey(key) && hasValue(obj.get(key));
    }

    private static boolean hasValue(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof String) {
            return StringUtils.isNotBlank((String) value);
        }
        return true;
    }

    private static void putIfPresent(JSONObject target, String key, Object value) {
        if (hasValue(value)) {
            target.put(key, value);
        }
    }

    private JSONObject parseContent(WorkOrderDynamic dynamic) {
        if (dynamic == null || StringUtils.isBlank(dynamic.getContentJson())) {
            return null;
        }
        try {
            return JSON.parseObject(dynamic.getContentJson());
        } catch (Exception e) {
            log.warn("BUGFIX-12345-004 动态contentJson解析失败，dynamicId={}", dynamic.getId(), e);
            return null;
        }
    }
    // BUGFIX-12345-004 END
}


