package top.tangyh.lamp.sop.service;

import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.model.vo.save.AudioVO;
import top.tangyh.lamp.model.vo.save.StatusUpdateVO;
import top.tangyh.lamp.sop.entity.SopIsvInfo;
import top.tangyh.lamp.sop.utils.RSATool;
import top.tangyh.lamp.sop.vo.result.SopIsvKeysResultVO;
import top.tangyh.lamp.sop.vo.save.SopIsvInfoApplyForVO;
import top.tangyh.lamp.sop.vo.save.SopIsvInfoSubmitVO;
import top.tangyh.lamp.sop.vo.update.SopIsvInfoOpenUpdateVO;


/**
 * <p>
 * 业务接口
 * isv信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
public interface SopIsvInfoService extends SuperService<Long, SopIsvInfo> {

    Boolean updateStatus(StatusUpdateVO param);

    Long examine(AudioVO param);

    RSATool.KeyStore createKeys(RSATool.KeyFormat keyFormat) throws Exception;

    SopIsvKeysResultVO getKeys(Long isvId);

    Long applyFor(SopIsvInfoApplyForVO param);

    Long submit(SopIsvInfoSubmitVO param);
    Long update(SopIsvInfoOpenUpdateVO param);
}


