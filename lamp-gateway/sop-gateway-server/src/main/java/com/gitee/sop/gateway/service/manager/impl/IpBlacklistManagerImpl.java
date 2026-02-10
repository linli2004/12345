package com.gitee.sop.gateway.service.manager.impl;

import com.gitee.sop.gateway.service.manager.IpBlacklistManager;
import org.springframework.stereotype.Service;

/**
 * @author 六如
 */
@Service
public class IpBlacklistManagerImpl implements IpBlacklistManager {
    @Override
    public boolean contains(String ip) {
        return false;
    }
}
