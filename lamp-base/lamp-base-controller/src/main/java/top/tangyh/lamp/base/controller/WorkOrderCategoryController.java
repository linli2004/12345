package top.tangyh.lamp.base.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.base.entity.WorkOrderCategory;
import top.tangyh.lamp.base.service.WorkOrderCategoryService;
import top.tangyh.lamp.base.vo.query.WorkOrderCategoryPageQuery;
import top.tangyh.lamp.base.vo.result.WorkOrderCategoryResultVO;
import top.tangyh.lamp.base.vo.save.WorkOrderCategorySaveVO;
import top.tangyh.lamp.base.vo.update.WorkOrderCategoryUpdateVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * 工单分类
 * </p>
 *
 * @author lunar
 * @date 2026-02-27 09:33:51
 * @create [2026-02-27 09:33:51] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/workOrderCategory")
@Tag(name = "工单分类")
public class WorkOrderCategoryController extends SuperController<WorkOrderCategoryService, Long, WorkOrderCategory
        , WorkOrderCategorySaveVO, WorkOrderCategoryUpdateVO, WorkOrderCategoryPageQuery, WorkOrderCategoryResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    /**
     * 按树结构查询
     *
     * @param pageQuery 查询参数
     * @return 查询结果
     */
    @Operation(summary = "按树结构查询", description = "按树结构查询")
    @PostMapping("/tree")
    @WebLog("级联查询")
    public R<List<WorkOrderCategory>> tree(@RequestBody WorkOrderCategoryPageQuery pageQuery) {
        return success(superService.findTree(pageQuery));
    }

    /**
     * 查询分类路径名称
     *
     * @param id 主键
     * @return 路径名称
     */
    @Operation(summary = "查询分类路径名称", description = "查询分类路径名称")
    @GetMapping("/pathName")
    @WebLog("查询分类路径名称")
    public R<Map<String, Object>> getPathName(@RequestParam Long id) {
        return success(superService.getPathName(id));
    }
}


