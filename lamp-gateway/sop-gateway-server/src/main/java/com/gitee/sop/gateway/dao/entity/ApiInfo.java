package com.gitee.sop.gateway.dao.entity;

import com.gitee.fastmybatis.annotation.Pk;
import com.gitee.fastmybatis.annotation.PkStrategy;
import com.gitee.fastmybatis.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：api_info
 * 备注：接口信息表
 *
 * @author 六如
 */
@Table(name = "sop_api_info", pk = @Pk(name = "id", strategy = PkStrategy.NONE))
@Data
public class ApiInfo {

    private Long id;

    /**
     * 所属应用
     */
    private String application;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 版本号
     */
    private String apiVersion;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 接口class
     */
    private String interfaceClassName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数信息
     */
    private String paramInfo;

    /**
     * 接口是否需要授权访问
     */
    private Integer isPermission;

    /**
     * 是否需要appAuthToken
     */
    private Integer isNeedToken;

    /**
     * 是否有公共响应参数
     */
    private Integer hasCommonResponse;

    /**
     * 注册来源，1-系统注册,2-手动注册
     */
    private Integer regSource;

    /**
     * 1启用，2禁用
     */
    private Integer status;

    /**
     * 接口模式，1-open接口，2-Restful模式
     */
    private Integer apiMode;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;


}
