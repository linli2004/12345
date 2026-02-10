package top.tangyh.lamp.sop.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.basic.utils.SpringUtils;
import top.tangyh.lamp.sop.entity.SopApiInfo;
import top.tangyh.lamp.sop.event.ChangeApiInfoEvent;
import top.tangyh.lamp.sop.vo.query.SopApiInfoPageQuery;
import top.tangyh.lamp.sop.vo.result.SopApiInfoResultVO;

import java.util.Collection;
import java.util.Collections;

/**
 * <p>
 * 通用业务接口
 * 接口信息表
 * </p>
 *
 * @author zuihou
 * @since [2025-05-07 10:52:47] [zuihou] [代码生成器生成]
 */
public interface SopApiInfoManager extends SuperManager<SopApiInfo> {

    default void sendChangeEvent(Long id) {
        SpringUtils.publishEvent(new ChangeApiInfoEvent(Collections.singletonList(id)));
    }

    default void sendChangeEvent(Collection<Long> idList) {
        SpringUtils.publishEvent(new ChangeApiInfoEvent(idList));
    }

    IPage<SopApiInfoResultVO> groupPage(IPage<SopApiInfo> page, SopApiInfoPageQuery query);
}


