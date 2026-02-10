package com.xxl.job.executor.service.jobhandler;

import cn.hutool.core.util.StrUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.tangyh.basic.context.ContextConstants;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.jackson.JsonUtil;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.msg.biz.MsgBiz;
import top.tangyh.lamp.msg.service.ExtendMsgService;

import java.util.Map;

@Component
@Slf4j
public class BaseJob {
    @Autowired
    private MsgBiz msgBiz;
    @Autowired
    private ExtendMsgService extendMsgService;

    @XxlJob("sendMsg")
    public void sendMsg() {
        String param = XxlJobHelper.getJobParam();
        ArgumentAssert.notEmpty(param, "参数不能为空");
        Map<String, String> map = JsonUtil.parse(param, Map.class);
        String tenantIdStr = map.get(ContextConstants.TENANT_ID_HEADER);
        String msgIdStr = map.get("msgId");
        if (StrUtil.isEmpty(tenantIdStr) || StrUtil.isEmpty(msgIdStr)) {
            return;
        }
        Long tenantId = Long.valueOf(tenantIdStr);
        Long msgId = Long.valueOf(msgIdStr);
        XxlJobHelper.log("tenantId={}, msgId={}", tenantId, msgId);

        ContextUtil.setTenantId(tenantId);

        msgBiz.execSend(msgId);
    }

    @XxlJob("publishMsg")
    public void publishMsg() {
        String param = XxlJobHelper.getJobParam();
        ArgumentAssert.notEmpty(param, "参数不能为空");
        Map<String, String> map = JsonUtil.parse(param, Map.class);
        String tenantIdStr = map.get(ContextConstants.TENANT_ID_HEADER);
        String msgIdStr = map.get("msgId");
        if (StrUtil.isEmpty(tenantIdStr) || StrUtil.isEmpty(msgIdStr)) {
            return;
        }
        Long tenantId = Long.valueOf(tenantIdStr);
        Long msgId = Long.valueOf(msgIdStr);
        XxlJobHelper.log("tenantId={}, msgId={}", tenantId, msgId);

        ContextUtil.setTenantId(tenantId);

        extendMsgService.publishNotice(msgId);
    }
}
