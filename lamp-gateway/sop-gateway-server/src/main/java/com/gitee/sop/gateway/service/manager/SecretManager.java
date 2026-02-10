package com.gitee.sop.gateway.service.manager;

import java.util.Collection;
import java.util.Map;

/**
 * 秘钥管理
 *
 * @author 六如
 */
public interface SecretManager extends Manager<Collection<Long>, Map<Long, String>> {

    /**
     * 获取用户上传的公钥
     *
     * @param isvId isvId
     * @return 返回公钥内容
     */
    String getIsvPublicKey(Long isvId);
}
