package top.tangyh.lamp.sop.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.basic.utils.BeanPlusUtil;
import top.tangyh.basic.utils.TreeUtil;
import top.tangyh.lamp.sop.entity.SopHelpDoc;
import top.tangyh.lamp.sop.service.SopHelpDocService;
import top.tangyh.lamp.sop.vo.query.SopHelpDocPageQuery;
import top.tangyh.lamp.sop.vo.result.SopHelpDocResultVO;
import top.tangyh.lamp.sop.vo.save.SopHelpDocSaveVO;
import top.tangyh.lamp.sop.vo.update.SopHelpDocUpdateVO;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * 帮助内容
 * </p>
 *
 * @author zuihou
 * @date 2025-12-18 12:21:28
 * @create [2025-12-18 12:21:28] [zuihou] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/sopHelpDoc")
@Tag(name = "帮助内容表")
public class SopHelpDocController extends SuperController<SopHelpDocService, Long, SopHelpDoc, SopHelpDocSaveVO, SopHelpDocUpdateVO, SopHelpDocPageQuery, SopHelpDocResultVO> {
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
    @Operation(summary = "按树结构查询")
    @PostMapping("/tree")
    @WebLog("按树结构查询")
    public R<List<SopHelpDocResultVO>> tree(@RequestBody SopHelpDocPageQuery pageQuery) {
        LbQueryWrap<SopHelpDoc> wrap = Wraps.lbQ();
        List<SopHelpDoc> list = superService.list(wrap);
        echoService.action(list);
        List<SopHelpDocResultVO> treeList = BeanPlusUtil.toBeanList(list, SopHelpDocResultVO.class);
        return success(TreeUtil.buildTree(treeList));
    }

}


