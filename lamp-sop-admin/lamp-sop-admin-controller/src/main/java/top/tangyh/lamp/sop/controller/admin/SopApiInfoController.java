package top.tangyh.lamp.sop.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.model.vo.save.StatusUpdateVO;
import top.tangyh.lamp.sop.entity.SopApiInfo;
import top.tangyh.lamp.sop.service.SopApiInfoService;
import top.tangyh.lamp.sop.vo.query.SopApiInfoPageQuery;
import top.tangyh.lamp.sop.vo.result.SopApiInfoResultVO;
import top.tangyh.lamp.sop.vo.save.SopApiInfoSaveVO;
import top.tangyh.lamp.sop.vo.update.SopApiInfoUpdateVO;

/**
 * <p>
 * 前端控制器
 * 接口信息表
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
@RequestMapping("/sopApiInfo")
@Tag(name = "接口信息表")
public class SopApiInfoController extends SuperController<SopApiInfoService, Long, SopApiInfo, SopApiInfoSaveVO, SopApiInfoUpdateVO, SopApiInfoPageQuery, SopApiInfoResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @PostMapping("/pageByGroup")
    @Operation(summary = "根据分组查询接口")
    @WebLog("根据分组查询接口")
    public R<IPage<SopApiInfoResultVO>> groupPage(@RequestBody @Validated(SopApiInfoPageQuery.GroupPage.class) PageParams<SopApiInfoPageQuery> params) {
        return R.success(superService.groupPage(params));
    }

    /**
     * 修改状态
     *
     * @param param 表单数据
     * @return 返回影响行数
     */
    @PostMapping("/updateStatus")
    public R<Boolean> updateStatus(@Validated @RequestBody StatusUpdateVO param) {
        return R.success(superService.updateStatus(param));
    }
}


