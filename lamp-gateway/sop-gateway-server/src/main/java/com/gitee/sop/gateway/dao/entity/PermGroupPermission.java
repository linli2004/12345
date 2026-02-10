package com.gitee.sop.gateway.dao.entity;

import com.gitee.fastmybatis.annotation.Pk;
import com.gitee.fastmybatis.annotation.PkStrategy;
import com.gitee.fastmybatis.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：perm_group_permission
 * 备注：组权限表
 *
 * @author 六如
 */
@Table(name = "sop_perm_group_permission", pk = @Pk(name = "id", strategy = PkStrategy.NONE))
@Data
public class PermGroupPermission {

    /**
     * id
     */
    private Long id;

    /**
     * 组id
     */
    private Long groupId;

    /**
     * api_info.id
     */
    private Long apiId;

    /**
     * 添加时间
     */
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    private LocalDateTime updatedTime;


}
