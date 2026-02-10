package com.gitee.sop.gateway.dao.entity;

import com.gitee.fastmybatis.annotation.Pk;
import com.gitee.fastmybatis.annotation.PkStrategy;
import com.gitee.fastmybatis.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：perm_isv_group
 * 备注：isv分组
 *
 * @author 六如
 */
@Table(name = "sop_perm_isv_group", pk = @Pk(name = "id", strategy = PkStrategy.NONE))
@Data
public class PermIsvGroup {

    private Long id;

    /**
     * isv_info表id
     */
    private Long isvId;

    /**
     * 组id
     */
    private Long groupId;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;


}
