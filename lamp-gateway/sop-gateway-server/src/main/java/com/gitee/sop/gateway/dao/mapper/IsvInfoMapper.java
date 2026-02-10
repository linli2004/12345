package com.gitee.sop.gateway.dao.mapper;

import com.gitee.fastmybatis.core.mapper.BaseMapper;
import com.gitee.sop.gateway.dao.entity.IsvInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 六如
 */
@Mapper
public interface IsvInfoMapper extends BaseMapper<IsvInfo> {

    default IsvInfo getByAppId(String appId) {
        return this.getByField(IsvInfo::getAppId, appId);
    }

}
