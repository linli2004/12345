package top.tangyh.lamp.sop.listener;

import com.gitee.sop.support.service.RefreshService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.lamp.sop.entity.SopIsvInfo;
import top.tangyh.lamp.sop.event.ChangeApiInfoEvent;
import top.tangyh.lamp.sop.event.ChangeIsvInfoEvent;
import top.tangyh.lamp.sop.event.ChangeIsvKeyEvent;
import top.tangyh.lamp.sop.event.ChangeIsvPermEvent;
import top.tangyh.lamp.sop.mapper.SopIsvInfoMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 变更监听
 *
 * @author zuihou
 */
@Component
public class ChangeApiListener {

    @DubboReference
    private RefreshService refreshService;

    @Autowired
    private SopIsvInfoMapper isvInfoMapper;

    /**
     * 监听isv信息变更
     *
     * @param event
     */
    @TransactionalEventListener
    public void onChangeApiInfoEvent(ChangeApiInfoEvent event) {
        Collection<Long> apiIds = event.apiIds();
        refreshService.refreshApi(apiIds);
    }

    /**
     * 监听isv信息变更
     *
     * @param event
     */
    @TransactionalEventListener
    public void onChangeIsvInfoEvent(ChangeIsvInfoEvent event) {
        Collection<Long> isvIds = event.isvIds();

        List<SopIsvInfo> sopIsvInfos = isvInfoMapper.selectList(Wraps.<SopIsvInfo>lbQ().in(SopIsvInfo::getId, isvIds));
        List<String> appIds = sopIsvInfos.stream().map(SopIsvInfo::getAppId).distinct().collect(Collectors.toList());
        refreshService.refreshIsv(appIds);
    }

    /**
     * 监听isv秘钥变更
     *
     * @param event
     */
    @TransactionalEventListener
    public void onChangeIsvKeyEvent(ChangeIsvKeyEvent event) {
        List<Long> isvIds = event.isvIds();
        refreshService.refreshSecret(isvIds);
    }

    /**
     * 监听isv分组变更
     *
     * @param event
     */
    @TransactionalEventListener
    public void onChangeIsvGroupEvent(ChangeIsvPermEvent event) {
        List<Long> isvIds = event.isvIds();
        refreshService.refreshIsvPerm(isvIds);
    }

}
