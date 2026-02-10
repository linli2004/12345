package top.tangyh.lamp.sop.controller.admin;

import cn.hutool.core.lang.tree.Tree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.model.vo.save.IdVO;
import top.tangyh.lamp.sop.entity.SopDocInfo;
import top.tangyh.lamp.sop.service.SopDocInfoService;
import top.tangyh.lamp.sop.vo.query.SopDocInfoPageQuery;
import top.tangyh.lamp.sop.vo.result.SopDocInfoResultVO;
import top.tangyh.lamp.sop.vo.save.SopDocInfoSaveVO;
import top.tangyh.lamp.sop.vo.update.SopDocInfoUpdateVO;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * 文档信息
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/sopDocInfo")
@Tag(name = "文档信息")
public class SopDocInfoController extends SuperController<SopDocInfoService, Long, SopDocInfo, SopDocInfoSaveVO, SopDocInfoUpdateVO, SopDocInfoPageQuery, SopDocInfoResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    /**
     * 按树结构查询
     *
     * @param docAppId 应用id
     * @return 查询结果
     */
    @Operation(summary = "按树结构查询", description = "按树结构查询")
    @PostMapping("/tree")
    @WebLog("按树结构查询")
    public R<List<Tree<Long>>> tree(@RequestParam Long docAppId) {
        return success(superService.tree(docAppId, null));
    }

    @Operation(summary = "发布文档", description = "发布文档")
    @PostMapping("publish")
    @WebLog("发布文档")
    public R<Boolean> publish(@RequestParam Long id, @RequestParam Integer isPublish) {
        return R.success(superService.publish(id, isPublish));
    }

    @Operation(summary = "同步应用文档", description = "同步应用文档")
    @PostMapping("syncAppDoc")
    @WebLog("同步应用文档")
    public R<Boolean> syncAppDoc(@Validated @RequestBody IdVO param) {
        superService.syncAppDoc(param.getId());
        return R.success();
    }

    @Operation(summary = "同步文档", description = "同步文档")
    @PostMapping("syncDoc")
    @WebLog("同步文档")
    public R<Boolean> syncDoc(@Validated @RequestBody IdVO param) {
        superService.syncDoc(param.getId());
        return R.success();
    }
}


