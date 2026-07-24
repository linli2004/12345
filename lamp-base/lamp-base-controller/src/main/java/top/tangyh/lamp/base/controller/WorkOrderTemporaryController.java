package top.tangyh.lamp.base.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.basic.utils.BeanPlusUtil;
import top.tangyh.lamp.base.entity.WorkOrderTemporary;
import top.tangyh.lamp.base.service.WorkOrderTemporaryService;
import top.tangyh.lamp.base.vo.query.WorkOrderTemporaryPageQuery;
import top.tangyh.lamp.base.vo.result.WorkOrderTemporaryResultVO;
import top.tangyh.lamp.base.vo.save.WorkOrderTemporarySaveVO;
import top.tangyh.lamp.base.vo.update.WorkOrderTemporaryUpdateVO;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * 工单办理暂存
 * </p>
 *
 * @author lunar
 * @date 2026-03-12 11:50:36
 * @create [2026-03-12 11:50:36] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/workOrderTemporary")
@Tag(name = "工单办理暂存")
public class WorkOrderTemporaryController extends SuperController<WorkOrderTemporaryService, Long, WorkOrderTemporary
        , WorkOrderTemporarySaveVO, WorkOrderTemporaryUpdateVO, WorkOrderTemporaryPageQuery, WorkOrderTemporaryResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Override
    public R query(WorkOrderTemporaryPageQuery data) {
        List<WorkOrderTemporary> list = superService.list(Wraps.<WorkOrderTemporary>lbQ()
                .eq(WorkOrderTemporary::getOrderNo, data.getOrderNo())
                .eq(WorkOrderTemporary::getNodeName, data.getNodeName())
                .eq(WorkOrderTemporary::getOperatorId, data.getOperatorId())
                .apply("FIND_IN_SET({0}, task_ids)", data.getTaskId())
                .orderByDesc(WorkOrderTemporary::getCreatedTime));
        if (!CollectionUtils.isEmpty(list)) {
            return success(BeanPlusUtil.toBeanList(list, getResultVOClass()).get(0));
        }

        // BUGFIX-12345-004: 结案待审无暂存时，构造单派结案审批默认值
        if ("结案待审结案".equals(data.getNodeName())) {
            WorkOrderTemporaryResultVO prefill = superService.buildFinishAuditTemporary(data);
            if (prefill != null) {
                return success(prefill);
            }
        }

        return success();
    }
}


