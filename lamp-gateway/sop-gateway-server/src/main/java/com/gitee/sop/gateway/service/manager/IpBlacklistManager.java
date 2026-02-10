package com.gitee.sop.gateway.service.manager;

/**
 * IP黑名单管理
 *
 * @author 六如
 */
public interface IpBlacklistManager {

    boolean contains(String ip);

}
