package top.tangyh.lamp.sop.manager;

import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.lamp.sop.entity.SopSysConfig;
import top.tangyh.lamp.sop.vo.result.DocSettingDTO;

/**
 * <p>
 * 通用业务接口
 * 系统配置表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:44
 *
 */
public interface SopSysConfigManager extends SuperManager<SopSysConfig> {
    SopSysConfig getByKey(String key);

    DocSettingDTO getDocSetting();
}


