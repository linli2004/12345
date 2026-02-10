package com.gitee.sop.gateway.service.validate;

import com.gitee.sop.gateway.common.ApiInfoDTO;
import com.gitee.sop.gateway.service.manager.dto.IsvDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 六如
 */
@AllArgsConstructor
@Getter
public class ValidateReturn {
    private ApiInfoDTO apiInfoDTO;
    private IsvDTO isvDTO;
}
