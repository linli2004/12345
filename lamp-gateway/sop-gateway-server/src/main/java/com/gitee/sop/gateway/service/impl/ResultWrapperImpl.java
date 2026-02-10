package com.gitee.sop.gateway.service.impl;

import com.gitee.sop.gateway.common.ApiInfoDTO;
import com.gitee.sop.gateway.common.RouteContext;
import com.gitee.sop.gateway.common.enums.YesOrNoEnum;
import com.gitee.sop.gateway.config.GateApiConfig;
import com.gitee.sop.gateway.response.ApiResponse;
import com.gitee.sop.gateway.response.NoCommonResponse;
import com.gitee.sop.gateway.response.Response;
import com.gitee.sop.gateway.service.ResultWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author 六如
 */
@Service
public class ResultWrapperImpl implements ResultWrapper {

    @Resource
    private GateApiConfig apiConfig;

    @Override
    public Response wrap(Optional<RouteContext> routeContextOpt, Object result) {
        boolean needNotWrap = routeContextOpt.map(RouteContext::getApiInfo)
                                      .map(ApiInfoDTO::getHasCommonResponse)
                                      .map(YesOrNoEnum::of)
                                      .orElse(YesOrNoEnum.YES) == YesOrNoEnum.NO;
        if (result instanceof ApiResponse) {
            ApiResponse apiResponse = (ApiResponse) result;
            return executeApiResponse(apiResponse, needNotWrap);
        }
        // 不需要公共返回参数
        if (needNotWrap) {
            return NoCommonResponse.success(result);
        }
        return ApiResponse.success(result);
    }

    private Response executeApiResponse(ApiResponse apiResponse, boolean needNotWrap) {
        // 不需要公共返回参数
        if (needNotWrap) {
            return NoCommonResponse.success(apiResponse.getData());
        }
        return apiResponse;
    }

}
