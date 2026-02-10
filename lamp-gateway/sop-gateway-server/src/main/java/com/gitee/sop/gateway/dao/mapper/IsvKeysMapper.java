package com.gitee.sop.gateway.dao.mapper;

import com.gitee.fastmybatis.core.mapper.BaseMapper;
import com.gitee.sop.gateway.dao.entity.IsvKeys;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 六如
 */
@Mapper
public interface IsvKeysMapper extends BaseMapper<IsvKeys> {

    default IsvKeys getByIsvId(Long isvId) {
        return this.getByField(IsvKeys::getIsvId, isvId);
    }

}
