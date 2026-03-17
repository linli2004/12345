package top.tangyh.lamp.base.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.base.entity.ChiefWorkOrderItem;
import top.tangyh.lamp.base.service.ChiefWorkOrderItemService;

import top.tangyh.basic.base.R;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.lamp.base.vo.query.ChiefWorkOrderItemPageQuery;
import top.tangyh.lamp.base.vo.result.ChiefWorkOrderItemResultVO;
import top.tangyh.lamp.base.vo.save.ChiefWorkOrderItemSaveVO;
import top.tangyh.lamp.base.vo.update.ChiefWorkOrderItemUpdateVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    private final ChiefWorkOrderItemService chiefWorkOrderItemService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Override
    public R<IPage<ChiefWorkOrderItemResultVO>> page(@RequestBody @Validated PageParams<ChiefWorkOrderItemPageQuery> params) {
        IPage<ChiefWorkOrderItemResultVO> pageResultVO = superService.findPageResultVO(params);
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
}
