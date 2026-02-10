package com.xxl.job.executor.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.xxl.job.core.context.XxlJobHelper;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import top.tangyh.basic.context.ContextConstants;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.lamp.model.enumeration.system.DefTenantStatusEnum;
import top.tangyh.lamp.system.entity.tenant.DefTenant;
import top.tangyh.lamp.system.service.tenant.DefTenantService;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author zuihou
 * @date 2021/1/4 11:35 下午
 */
public abstract class AbstractTenantJob {
    @Autowired
    private DefTenantService defTenantService;

    protected void loadTenant(BiConsumer<DefTenant, String> consumer) {
        String traceId = IdUtil.fastSimpleUUID();
        MDC.put(ContextConstants.TRACE_ID_HEADER, StrUtil.isEmpty(traceId) ? StrUtil.EMPTY : traceId);

        LbQueryWrap<DefTenant> wrapper = Wraps.<DefTenant>lbQ()
                .eq(DefTenant::getStatus, DefTenantStatusEnum.NORMAL.getCode());

        List<DefTenant> list = defTenantService.list(wrapper);

        list.forEach(tenant -> {
            MDC.put(ContextConstants.TENANT_ID_KEY, tenant.getCode());
            consumer.accept(tenant, XxlJobHelper.getJobParam());
        });
    }

}
