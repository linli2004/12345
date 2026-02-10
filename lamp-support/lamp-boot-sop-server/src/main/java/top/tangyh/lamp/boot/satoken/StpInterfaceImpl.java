package top.tangyh.lamp.boot.satoken;

import cn.dev33.satoken.stp.StpInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tangyh.lamp.oauth.biz.StpInterfaceBiz;

import java.util.List;

/**
 * sa-token 权限实现
 *
 * @author tangyh
 * @since 2024/7/26 17:35
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StpInterfaceImpl implements StpInterface {
    private final StpInterfaceBiz stpInterfaceBiz;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return stpInterfaceBiz.getPermissionList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return stpInterfaceBiz.getRoleList();
    }
}
