package top.tangyh.lamp.sop.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.utils.CollHelper;
import top.tangyh.lamp.sop.entity.SopSysConfig;
import top.tangyh.lamp.sop.enums.ConfigKeyEnum;
import top.tangyh.lamp.sop.manager.SopSysConfigManager;
import top.tangyh.lamp.sop.mapper.SopSysConfigMapper;
import top.tangyh.lamp.sop.vo.result.DocSettingDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通用业务实现类
 * 系统配置表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:44
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SopSysConfigManagerImpl extends SuperManagerImpl<SopSysConfigMapper, SopSysConfig> implements SopSysConfigManager {

    @Override
    public SopSysConfig getByKey(String key) {
        return this.getOne(Wraps.<SopSysConfig>lbQ().eq(SopSysConfig::getConfigKey, key));
    }

    @Override
    public DocSettingDTO getDocSetting() {
        List<SopSysConfig> list = this.list(Wraps.<SopSysConfig>lbQ().in(SopSysConfig::getConfigKey, ConfigKeyEnum.TORNA_SERVER_ADDR.getKey(),
                ConfigKeyEnum.OPEN_PROD_URL.getKey(), ConfigKeyEnum.OPEN_SANDBOX_URL.getKey()
        ));
        Map<String, String> map = CollHelper.buildMap(list, SopSysConfig::getConfigKey, SopSysConfig::getConfigValue);
        DocSettingDTO docSettingDTO = new DocSettingDTO();
        docSettingDTO.setTornaServerAddr(map.get(ConfigKeyEnum.TORNA_SERVER_ADDR.getKey()));
        docSettingDTO.setOpenProdUrl(map.get(ConfigKeyEnum.OPEN_PROD_URL.getKey()));
        docSettingDTO.setOpenSandboxUrl(map.get(ConfigKeyEnum.OPEN_SANDBOX_URL.getKey()));
        return docSettingDTO;
    }
}


