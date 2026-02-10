package com.gitee.sop.gateway.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.sop.gateway.request.ApiRequestContext;
import com.gitee.sop.gateway.service.validate.ValidateReturn;
import org.springframework.stereotype.Service;

/**
 * @author 六如
 */
@Service("restRouteService")
public class RestRouteServiceImpl extends RouteServiceImpl {

    @Override
    protected ValidateReturn validate(ApiRequestContext apiRequestContext) {
        return validator.validateRest(apiRequestContext);
    }

    @Override
    protected JSONObject getParamObject(ApiRequestContext apiRequestContext) {
        return apiRequestContext.getRawParams();
    }
}
