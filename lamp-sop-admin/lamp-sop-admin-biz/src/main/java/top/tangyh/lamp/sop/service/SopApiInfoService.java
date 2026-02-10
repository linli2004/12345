package top.tangyh.lamp.sop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.model.vo.save.StatusUpdateVO;
import top.tangyh.lamp.sop.entity.SopApiInfo;
import top.tangyh.lamp.sop.vo.query.SopApiInfoPageQuery;
import top.tangyh.lamp.sop.vo.result.SopApiInfoResultVO;


/**
 * <p>
 * 业务接口
 * 接口信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
public interface SopApiInfoService extends SuperService<Long, SopApiInfo> {
    Boolean updateStatus(StatusUpdateVO statusUpdate);

    IPage<SopApiInfoResultVO> groupPage(PageParams<SopApiInfoPageQuery> params);
}


