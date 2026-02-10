package top.tangyh.lamp.system.entity.application;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.Entity;

import java.time.LocalDateTime;

/**
 * <p>
 * 实体类
 * 租户的资源
 * </p>
 *
 * @author zuihou
 * @since 2021-09-15
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("def_tenant_resource_rel")
@AllArgsConstructor
public class DefTenantResourceRel extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    @TableField(value = "tenant_id")
    private Long tenantId;

    /**
     * 应用Id
     */
    @TableField(value = "application_id")
    private Long applicationId;

    /**
     * 资源ID
     */
    @TableField(value = "resource_id")
    private Long resourceId;


    @Builder
    public DefTenantResourceRel(Long id, Long createdBy, LocalDateTime createdTime, Long updatedBy, LocalDateTime updatedTime,
                                Long tenantId, Long applicationId, Long resourceId) {
        this.id = id;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
        this.tenantId = tenantId;
        this.applicationId = applicationId;
        this.resourceId = resourceId;
    }

}
