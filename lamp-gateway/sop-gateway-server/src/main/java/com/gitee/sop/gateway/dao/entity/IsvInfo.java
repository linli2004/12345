package com.gitee.sop.gateway.dao.entity;

import com.gitee.fastmybatis.annotation.Pk;
import com.gitee.fastmybatis.annotation.PkStrategy;
import com.gitee.fastmybatis.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：isv_info
 * 备注：isv信息表
 *
 * @author 六如
 */
@Table(name = "sop_isv_info", pk = @Pk(name = "id", strategy = PkStrategy.NONE))
@Data
public class IsvInfo {

    private Long id;

    /**
     * appKey
     */
    private String appId;

    /**
     * 1启用，2禁用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
    private LocalDateTime startExpirationTime;
    private LocalDateTime endExpirationTime;
    private Integer auditStatus;
    private LocalDateTime auditTime;
    private LocalDateTime submissionTime;
    private Integer creationMethod;
    private String reviewComments;
    private Long tenantId;
    private String name;
    /**
     * 回调接口
     */
    private String notifyUrl;
}
