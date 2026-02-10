package com.gitee.sop.gateway.service.manager;

import com.gitee.sop.gateway.service.manager.dto.IsvDTO;

import java.util.Collection;
import java.util.Map;

/**
 * @author 六如
 */
public interface IsvManager extends Manager<Collection<String>, Map<String, IsvDTO>> {

    /**
     * 获取isv信息
     *
     * @param appId appId
     * @return 返回isv信息, 没有返回null
     */
    IsvDTO getIsv(String appId);
}
