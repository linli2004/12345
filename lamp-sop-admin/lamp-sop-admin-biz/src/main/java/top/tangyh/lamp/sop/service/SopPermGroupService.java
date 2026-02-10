package top.tangyh.lamp.sop.service;

import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.sop.entity.SopPermGroup;
import top.tangyh.lamp.sop.vo.save.SopPermIsvGroupSaveVO;


/**
 * <p>
 * 业务接口
 * 分组表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
public interface SopPermGroupService extends SuperService<Long, SopPermGroup> {

    Boolean updateIsvGroup(SopPermIsvGroupSaveVO param);
}


