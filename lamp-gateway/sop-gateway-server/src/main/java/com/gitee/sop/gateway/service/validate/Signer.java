package com.gitee.sop.gateway.service.validate;

import com.gitee.sop.gateway.request.ApiRequestContext;

/**
 * 负责签名校验
 *
 * @author 六如
 */
public interface Signer {

    /**
     * 签名校验
     *
     * @param apiRequestContext 参数
     * @param publicKey         公钥
     * @return true签名正确
     */
    boolean checkSign(ApiRequestContext apiRequestContext, String publicKey);

}
