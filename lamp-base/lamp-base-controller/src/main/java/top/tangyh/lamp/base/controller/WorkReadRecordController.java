package top.tangyh.lamp.base.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.base.controller.SuperSimpleController;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.Constant;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.entity.NormalWorkOrderTask;
import top.tangyh.lamp.base.entity.WorkReadRecord;
import top.tangyh.lamp.base.service.NormalWorkOrderService;
import top.tangyh.lamp.base.service.NormalWorkOrderTaskService;
import top.tangyh.lamp.base.service.WorkReadRecordService;
import top.tangyh.lamp.base.vo.query.NormalWorkOrderPageQuery;
import top.tangyh.lamp.base.vo.result.NormalWorkOrderResultVO;
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
@RequestMapping("/workReadRecord")
@Tag(name = "工单已读记录")
public class WorkReadRecordController extends SuperSimpleController<WorkReadRecordService, Long, WorkReadRecord> {


    /**
     * 查询工单是否已读
     *
     * @param workReadRecord 查询工单是否已读
     */
    @Operation(summary = "查询工单是否已读", description = "查询工单是否已读")
    @PostMapping("/checkRead")
    public R<List<Boolean>> checkRead(@RequestBody @Validated List<WorkReadRecord> workReadRecord) {
       return R.success(superService.checkRead(workReadRecord));
    }

    @Operation(summary = "新增工单是否已读", description = "新增工单是否已读")
    @PostMapping("/save")
    public R<Boolean> save(@RequestBody @Validated List<WorkReadRecord> workReadRecord) {
        return R.success(superService.saveBatch(workReadRecord));
    }
}


