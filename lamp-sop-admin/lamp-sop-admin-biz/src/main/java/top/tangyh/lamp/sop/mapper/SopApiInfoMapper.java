package top.tangyh.lamp.sop.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.tangyh.basic.base.mapper.SuperMapper;
import top.tangyh.lamp.sop.entity.SopApiInfo;
import top.tangyh.lamp.sop.vo.query.SopApiInfoPageQuery;
import top.tangyh.lamp.sop.vo.result.SopApiInfoResultVO;

/**
 * <p>
 * Mapper 接口
 * 接口信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Repository
public interface SopApiInfoMapper extends SuperMapper<SopApiInfo> {

    IPage<SopApiInfoResultVO> groupPage(IPage<SopApiInfo> page,
                                        @Param(Constants.WRAPPER) Wrapper<SopApiInfo> wrapper,
                                        @Param("query") SopApiInfoPageQuery query);

}


