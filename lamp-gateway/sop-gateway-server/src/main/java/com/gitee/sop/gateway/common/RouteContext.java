package com.gitee.sop.gateway.common;

import com.gitee.sop.gateway.request.ApiRequestContext;
import com.gitee.sop.gateway.service.manager.dto.IsvDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 六如
 */
@Getter
@AllArgsConstructor
public class RouteContext {

    private ApiRequestContext apiRequestContext;
    private ApiInfoDTO apiInfo;
    private IsvDTO isv;

}
