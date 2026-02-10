package top.tangyh.lamp.base.controller.common;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.base.entity.common.BaseParameter;
import top.tangyh.lamp.base.service.common.BaseParameterService;
import top.tangyh.lamp.base.vo.query.common.BaseParameterPageQuery;
import top.tangyh.lamp.base.vo.result.common.BaseParameterResultVO;
import top.tangyh.lamp.base.vo.save.common.BaseParameterSaveVO;
import top.tangyh.lamp.base.vo.update.common.BaseParameterUpdateVO;


/**
 * <p>
 * 前端控制器
 * 个性参数
 * </p>
 *
 * @author zuihou
 * @date 2021-11-08
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/baseParameter")
@Tag(name = "个性参数")
public class BaseParameterController extends SuperController<BaseParameterService, Long, BaseParameter, BaseParameterSaveVO, BaseParameterUpdateVO, BaseParameterPageQuery, BaseParameterResultVO> {

    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

}
