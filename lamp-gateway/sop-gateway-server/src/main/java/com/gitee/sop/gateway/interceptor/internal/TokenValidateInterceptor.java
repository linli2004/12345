package com.gitee.sop.gateway.interceptor.internal;

import com.gitee.sop.gateway.common.ApiInfoDTO;
import com.gitee.sop.gateway.common.RouteContext;
import com.gitee.sop.gateway.common.enums.YesOrNoEnum;
import com.gitee.sop.gateway.exception.ApiException;
import com.gitee.sop.gateway.interceptor.RouteInterceptor;
import com.gitee.sop.gateway.message.ErrorEnum;
import com.gitee.sop.gateway.request.ApiRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 校验token
 *
 * @author 六如
 */
@Component
@Slf4j
public class TokenValidateInterceptor implements RouteInterceptor {
    @Override
    public void preRoute(RouteContext routeContext) {
        ApiRequestContext apiRequestContext = routeContext.getApiRequestContext();
        ApiInfoDTO apiInfo = routeContext.getApiInfo();
        // 走到这里token肯定有值
        String appAuthToken = apiRequestContext.getApiRequest().getAppAuthToken();

        if (!checkToken(appAuthToken, apiRequestContext, apiInfo)) {
            throw new ApiException(ErrorEnum.AOP_INVALID_AUTH_TOKEN, apiRequestContext.getLocale());
        }
    }

    @Override
    public boolean match(RouteContext routeContext) {
        ApiInfoDTO apiInfo = routeContext.getApiInfo();
        Integer isNeedToken = apiInfo.getIsNeedToken();
        return YesOrNoEnum.of(isNeedToken) == YesOrNoEnum.YES;
    }

    /**
     * 校验token是否合法
     *
     * @param appAuthToken token
     * @param context      上下文
     * @param apiInfoDTO   接口信息
     * @return 返回true表示token合法，false不合法
     */
    protected boolean checkToken(String appAuthToken, ApiRequestContext context, ApiInfoDTO apiInfoDTO) {
        // 这里做校验token操作，如从redis查询token是否存在

        return true;
    }
}
