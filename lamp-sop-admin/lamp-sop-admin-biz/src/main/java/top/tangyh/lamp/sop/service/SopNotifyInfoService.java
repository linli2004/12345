package top.tangyh.lamp.sop.service;

import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.sop.entity.SopNotifyInfo;


/**
 * <p>
 * 业务接口
 * 回调信息
 * </p>
 *
 * @author zuihou
 * @date 2025-12-17 15:38:07
 * @create [2025-12-17 15:38:07] [zuihou] [代码生成器生成]
 */
public interface SopNotifyInfoService extends SuperService<Long, SopNotifyInfo> {

    Boolean end(Long id);

    Long push(Long id, String url);
}


