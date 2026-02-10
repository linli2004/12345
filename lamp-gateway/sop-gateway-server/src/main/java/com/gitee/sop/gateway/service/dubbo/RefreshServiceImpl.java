package com.gitee.sop.gateway.service.dubbo;

import com.gitee.sop.gateway.service.manager.ApiManager;
import com.gitee.sop.gateway.service.manager.IsvApiPermissionManager;
import com.gitee.sop.gateway.service.manager.IsvManager;
import com.gitee.sop.gateway.service.manager.SecretManager;
import com.gitee.sop.support.service.RefreshService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Collection;

/**
 * @author 六如
 */
@DubboService
@Slf4j
public class RefreshServiceImpl implements RefreshService {

    @Resource
    private IsvManager isvManager;
    @Resource
    private SecretManager secretManager;
    @Resource
    private IsvApiPermissionManager isvApiPermissionManager;
    @Resource
    private ApiManager apiManager;

    @Override
    public void refreshApi(Collection<Long> apiIds) {
        apiManager.refresh(apiIds);
    }

    @Override
    public void refreshIsv(Collection<String> appIds) {
        isvManager.refresh(appIds);
    }

    @Override
    public void refreshIsvPerm(Collection<Long> isvIds) {
        isvApiPermissionManager.refresh(isvIds);
    }


    @Override
    public void refreshSecret(Collection<Long> isvIds) {
        secretManager.refresh(isvIds);
    }
}
