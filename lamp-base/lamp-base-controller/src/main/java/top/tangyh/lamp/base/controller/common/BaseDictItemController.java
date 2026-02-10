package top.tangyh.lamp.base.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.basic.database.mybatis.conditions.query.QueryWrap;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.base.entity.common.BaseDict;
import top.tangyh.lamp.base.service.common.BaseDictItemService;
import top.tangyh.lamp.base.vo.query.common.BaseDictItemPageQuery;
import top.tangyh.lamp.base.vo.result.common.BaseDictItemResultVO;
import top.tangyh.lamp.base.vo.save.common.BaseDictItemSaveVO;
import top.tangyh.lamp.base.vo.update.common.BaseDictItemUpdateVO;


/**
 * <p>baseDict
 * 前端控制器
 * 字典项
 * </p>
 *
 * @author zuihou
 * @date 2021-10-04
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/baseDictItem")
@Tag(name = "字典项")
public class BaseDictItemController extends SuperController<BaseDictItemService, Long, BaseDict,
        BaseDictItemSaveVO, BaseDictItemUpdateVO, BaseDictItemPageQuery, BaseDictItemResultVO> {

    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Override
    public void handlerQueryParams(PageParams<BaseDictItemPageQuery> params) {
        ArgumentAssert.notNull(params.getModel().getParentId(), "请选择字典");
    }

    @Override
    public QueryWrap<BaseDict> handlerWrapper(BaseDict model, PageParams<BaseDictItemPageQuery> params) {
        QueryWrap<BaseDict> wrap = super.handlerWrapper(null, params);
        LbQueryWrap<BaseDict> wrapper = wrap.lambda();
        wrapper.eq(BaseDict::getParentId, model.getParentId())
                .like(BaseDict::getKey, model.getKey())
                .like(BaseDict::getName, model.getName())
                .in(BaseDict::getClassify, params.getModel().getClassify())
                .in(BaseDict::getState, params.getModel().getState());
        return wrap;
    }

    @Parameters({
            @Parameter(name = "id", description = "ID", schema = @Schema(type = "long"), in = ParameterIn.QUERY),
            @Parameter(name = "dictId", description = "字典ID", required = true, schema = @Schema(type = "long"), in = ParameterIn.QUERY),
            @Parameter(name = "key", description = "字典标识", required = true, schema = @Schema(type = "string"), in = ParameterIn.QUERY),
    })
    @Operation(summary = "检测字典项标识是否可用", description = "检测字典项标识是否可用")
    @GetMapping("/check")
    public R<Boolean> check(@RequestParam String key, @RequestParam Long dictId, @RequestParam(required = false) Long id) {
        return success(superService.checkItemByKey(key, dictId, id));
    }

}
