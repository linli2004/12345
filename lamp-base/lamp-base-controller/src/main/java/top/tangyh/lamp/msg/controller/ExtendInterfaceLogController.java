package top.tangyh.lamp.msg.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.database.mybatis.conditions.query.QueryWrap;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.msg.entity.ExtendInterfaceLog;
import top.tangyh.lamp.msg.service.ExtendInterfaceLogService;
import top.tangyh.lamp.msg.vo.query.ExtendInterfaceLogPageQuery;
import top.tangyh.lamp.msg.vo.result.ExtendInterfaceLogResultVO;
import top.tangyh.lamp.msg.vo.save.ExtendInterfaceLogSaveVO;
import top.tangyh.lamp.msg.vo.update.ExtendInterfaceLogUpdateVO;

/**
 * <p>
 * 前端控制器
 * 接口执行日志
 * </p>
 *
 * @author zuihou
 * @date 2022-07-09 23:58:59
 * @create [2022-07-09 23:58:59] [zuihou] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/extendInterfaceLog")
@Tag(name = "接口执行日志")
public class ExtendInterfaceLogController extends SuperController<ExtendInterfaceLogService, Long, ExtendInterfaceLog, ExtendInterfaceLogSaveVO,
        ExtendInterfaceLogUpdateVO, ExtendInterfaceLogPageQuery, ExtendInterfaceLogResultVO> {
    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

    @Override
    public QueryWrap<ExtendInterfaceLog> handlerWrapper(ExtendInterfaceLog model, PageParams<ExtendInterfaceLogPageQuery> params) {
        QueryWrap<ExtendInterfaceLog> queryWrap = super.handlerWrapper(model, params);
        Long tenantId = params.getModel().getTenantId();
        if (tenantId != null) {
            ContextUtil.setTenantBasePoolName(tenantId);
        }
        return queryWrap;
    }

}


