package com.gitee.sop.gateway.dao.mapper;


import com.gitee.fastmybatis.core.mapper.BaseMapper;
import com.gitee.sop.gateway.dao.entity.ApiInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 六如
 */
@Mapper
public interface ApiInfoMapper extends BaseMapper<ApiInfo> {

    default ApiInfo getByNameVersion(String apiName, String apiVersion) {
        return this.query()
                .eq(ApiInfo::getApiName, apiName)
                .eq(ApiInfo::getApiVersion, apiVersion)
                .get();
    }

}
