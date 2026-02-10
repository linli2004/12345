package top.tangyh.lamp.tenant.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.lamp.tenant.service.DataSourceService;

import java.util.Set;

/**
 * 数据源初始化
 *
 * @author zuihou
 * @date 2020年04月05日16:34:04
 */
@Slf4j
@RestController
@RequestMapping("/ds")
@Tag(name = "数据源")
@RequiredArgsConstructor
public class TenantDsController {

    private final DataSourceService dataSourceService;


    @GetMapping(value = "/remove")
    @Operation(summary = "删除数据源", description = "删除数据源")
    public R<Boolean> remove(@RequestParam("tenantId") Long tenantId) {
        return R.success(dataSourceService.removeDbAndDs(tenantId));
    }

    @GetMapping
    @Operation(summary = "获取当前服务的所有数据源", description = "获取当前服务的所有数据源")
    public R<Set<String>> list() {
        return R.success(dataSourceService.findAll());
    }

    @Operation(summary = "初始化数据源", description = "初始化数据源")
    @GetMapping(value = "/initDataSource")
    public R<Boolean> initDataSource(@RequestParam(value = "tenantId") Long tenantId) {
        return R.success(dataSourceService.initDataSource(tenantId));
    }

    @GetMapping("/check")
    @Operation(summary = "检测是否存在指定数据源", description = "检测是否存在指定数据源")
    public R<Boolean> check(@RequestParam(value = "tenantId") Long tenantId) {
        return R.success(dataSourceService.check(tenantId));
    }

}
