package com.gitee.sop.gateway.service;

import com.gitee.sop.gateway.common.RouteContext;
import com.gitee.sop.gateway.response.Response;

import java.util.Optional;

/**
 * 结果包裹
 *
 * @author 六如
 */
public interface ResultWrapper {

    Response wrap(Optional<RouteContext> routeContextOpt, Object result);

}
