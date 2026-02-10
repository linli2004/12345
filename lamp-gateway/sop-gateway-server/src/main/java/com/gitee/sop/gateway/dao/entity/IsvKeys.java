package com.gitee.sop.gateway.dao.entity;

import com.gitee.fastmybatis.annotation.Pk;
import com.gitee.fastmybatis.annotation.PkStrategy;
import com.gitee.fastmybatis.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：isv_keys
 * 备注：ISV秘钥管理
 *
 * @author 六如
 */
@Table(name = "sop_isv_keys", pk = @Pk(name = "id", strategy = PkStrategy.NONE))
@Data
public class IsvKeys {

    /**
     * id
     */
    private Long id;

    /**
     * isv_info.id
     */
    private Long isvId;

    /**
     * 秘钥格式，1：PKCS8(JAVA适用)，2：PKCS1(非JAVA适用)
     */
    private Integer keyFormat;

    /**
     * 开发者生成的公钥
     */
    private String publicKeyIsv;

    /**
     * 开发者生成的私钥（交给开发者）
     */
    private String privateKeyIsv;

    /**
     * 平台生成的公钥（交给开发者）
     */
    private String publicKeyPlatform;

    /**
     * 平台生成的私钥
     */
    private String privateKeyPlatform;

    /**
     * 添加时间
     */
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    private LocalDateTime updatedTime;


}
