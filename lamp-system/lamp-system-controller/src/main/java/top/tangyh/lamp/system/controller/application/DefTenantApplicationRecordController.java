package top.tangyh.lamp.system.controller.application;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.controller.SuperReadController;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.lamp.system.entity.application.DefTenantApplicationRecord;
import top.tangyh.lamp.system.service.application.DefTenantApplicationRecordService;
import top.tangyh.lamp.system.vo.query.application.DefTenantApplicationRecordPageQuery;
import top.tangyh.lamp.system.vo.result.application.DefTenantApplicationRecordResultVO;


/**
 * <p>
 * 前端控制器
 * 租户应用授权记录
 * </p>
 *
 * @author zuihou
 * @date 2021-09-15
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/defTenantApplicationRecord")
@Tag(name = "租户应用授权记录")
public class DefTenantApplicationRecordController extends SuperReadController
        <DefTenantApplicationRecordService, Long, DefTenantApplicationRecord, DefTenantApplicationRecordPageQuery, DefTenantApplicationRecordResultVO> {

    private final EchoService echoService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }

}

