package top.tangyh.lamp.sop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.lamp.sop.entity.SopDocApp;
import top.tangyh.lamp.sop.manager.SopDocAppManager;
import top.tangyh.lamp.sop.manager.TornaClient;
import top.tangyh.lamp.sop.service.SopDocAppService;
import top.tangyh.lamp.sop.service.SopDocInfoService;
import top.tangyh.lamp.sop.vo.result.TornaModuleDTO;

/**
 * <p>
 * 业务实现类
 * 文档应用
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SopDocAppServiceImpl extends SuperServiceImpl<SopDocAppManager, Long, SopDocApp> implements SopDocAppService {
    private final TornaClient tornaClient;
    private final SopDocInfoService sopDocInfoService;

    @Override
    public <SaveVO> SopDocApp save(SaveVO saveVO) {
        SopDocApp sopDocApp = super.saveBefore(saveVO);

        TornaModuleDTO tornaModuleDTO = tornaClient.execute("module.get", null, sopDocApp.getToken(), TornaModuleDTO.class);
        SopDocApp docApp = superManager.getOne(Wraps.<SopDocApp>lbQ().eq(SopDocApp::getToken, sopDocApp.getToken()));
        if (docApp == null) {
            docApp = new SopDocApp();
            docApp.setAppName(tornaModuleDTO.getName());
            docApp.setToken(sopDocApp.getToken());
            superManager.save(docApp);
        } else {
            docApp.setAppName(tornaModuleDTO.getName());
            superManager.updateById(docApp);
            sopDocApp.setId(docApp.getId());
        }
        // 同步文档
        sopDocInfoService.syncDocInfo(docApp, null);
        return sopDocApp;
    }


}


