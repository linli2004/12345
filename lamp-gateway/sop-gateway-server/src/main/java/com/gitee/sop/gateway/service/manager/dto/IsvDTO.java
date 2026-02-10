package com.gitee.sop.gateway.service.manager.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 六如
 */
@Data
public class IsvDTO {

    private Long id;

    private String appId;

    private Long tenantId;
    private Integer status;

    /** 开始有效期 */
    private LocalDateTime startExpirationTime;
    /** 结束有效期 */
    private LocalDateTime endExpirationTime;

}
