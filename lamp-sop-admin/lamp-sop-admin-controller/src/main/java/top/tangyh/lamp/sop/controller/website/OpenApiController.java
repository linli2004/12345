package top.tangyh.lamp.sop.controller.website;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.basic.utils.BeanPlusUtil;
import top.tangyh.basic.utils.TreeUtil;
import top.tangyh.lamp.model.enumeration.BooleanEnum;
import top.tangyh.lamp.sop.entity.SopDocApp;
import top.tangyh.lamp.sop.entity.SopHelpDoc;
import top.tangyh.lamp.sop.service.SopDocAppService;
import top.tangyh.lamp.sop.service.SopDocInfoService;
import top.tangyh.lamp.sop.service.SopHelpDocService;
import top.tangyh.lamp.sop.vo.query.SopHelpDocPageQuery;
import top.tangyh.lamp.sop.vo.result.DocInfoViewVO;
import top.tangyh.lamp.sop.vo.result.SopDocAppResultVO;
import top.tangyh.lamp.sop.vo.result.SopHelpDocResultVO;

import java.util.List;

/**
 * 对完
 * @author tangyh
 * @since 2025/5/15 18:00
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/anyTenant/website")
@Tag(name = "接口信息表")
public class OpenApiController {
    private final SopDocAppService sopDocAppService;
    private final SopDocInfoService sopDocInfoService;
    private final SopHelpDocService sopHelpDocService;


    /**
     * 获取文档应用列表
     */
    @GetMapping(value = "/app/list")
    public R<List<SopDocAppResultVO>> findAppList() {
        List<SopDocApp> list = sopDocAppService.list(Wrappers.emptyWrapper());
        return R.success(BeanUtil.copyToList(list, SopDocAppResultVO.class));
    }

    /**
     * 获取文档菜单树
     *
     * @param docAppId 应用id
     */
    @GetMapping(value = "/doc/tree")
    public R<List<Tree<Long>>> findDocTree(@RequestParam Long docAppId) {
        return R.success(sopDocInfoService.tree(docAppId, BooleanEnum.TRUE.getInteger()));
    }

    /**
     * 按树结构查询
     *
     * @param pageQuery 查询参数
     * @return 查询结果
     */
    @Operation(summary = "按树结构查询")
    @PostMapping("/help/tree")
    public R<List<SopHelpDocResultVO>> tree(@RequestBody SopHelpDocPageQuery pageQuery) {
        LbQueryWrap<SopHelpDoc> wrap = Wraps.lbQ();
        List<SopHelpDoc> list = sopHelpDocService.list(wrap);
        List<SopHelpDocResultVO> treeList = BeanPlusUtil.toBeanList(list, SopHelpDocResultVO.class);
        return R.success(TreeUtil.buildTree(treeList));
    }

    /**
     * 获取文档详情
     *
     * @param id 文档id
     */
    @GetMapping("/doc/detail")
    public R<DocInfoViewVO> getDocDetail(@RequestParam Long id) {
        return R.success(sopDocInfoService.getDocView(id));
    }
}
