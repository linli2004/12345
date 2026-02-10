package top.tangyh.lamp.sop.controller.admin;

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
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.sop.entity.SopPermGroup;
import top.tangyh.lamp.sop.service.SopPermGroupService;
import top.tangyh.lamp.sop.service.SopPermIsvGroupService;
import top.tangyh.lamp.sop.vo.query.SopPermGroupPageQuery;
import top.tangyh.lamp.sop.vo.result.SopPermGroupResultVO;
import top.tangyh.lamp.sop.vo.save.SopPermGroupSaveVO;
import top.tangyh.lamp.sop.vo.save.SopPermIsvGroupSaveVO;
import top.tangyh.lamp.sop.vo.update.SopPermGroupUpdateVO;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * 分组表
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
@RequestMapping("/sopPermGroup")
@Tag(name = "分组表")
public class SopPermGroupController extends SuperController<SopPermGroupService, Long, SopPermGroup, SopPermGroupSaveVO, SopPermGroupUpdateVO, SopPermGroupPageQuery, SopPermGroupResultVO> {
    private final EchoService echoService;
    private final SopPermIsvGroupService sopPermIsvGroupService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @GetMapping("/listByGroupId")
    public R<List<Long>> listByGroupId(@RequestParam Long isvId) {
        return R.success(sopPermIsvGroupService.listByGroupId(isvId));
    }


    /**
     * 设置分组
     *
     * @param param 表单数据
     * @return 返回影响行数
     */
    @PostMapping("updateIsvGroup")
    public R<Boolean> updateIsvGroup(@Validated @RequestBody SopPermIsvGroupSaveVO param) {
        return R.success(superService.updateIsvGroup(param));
    }
}


