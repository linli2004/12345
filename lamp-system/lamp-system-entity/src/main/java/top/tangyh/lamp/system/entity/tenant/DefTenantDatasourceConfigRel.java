package top.tangyh.lamp.system.entity.tenant;

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

import static top.tangyh.lamp.model.constant.Condition.LIKE;

/**
 * <p>
 * 实体类
 * 租户的数据源
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
@TableName("def_tenant_ds_c_rel")
@AllArgsConstructor
public class DefTenantDatasourceConfigRel extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    private Long tenantId;

    /**
     * 数据源id
     */
    @TableField(value = "datasource_config_id")
    private Long datasourceConfigId;

    @TableField(value = "db_prefix", condition = LIKE)
    private String dbPrefix;


    @Builder
    public DefTenantDatasourceConfigRel(Long id, LocalDateTime createdTime, Long createdBy, LocalDateTime updatedTime, Long updatedBy,
                                        Long tenantId, Long datasourceConfigId, String dbPrefix) {
        this.id = id;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.updatedTime = updatedTime;
        this.updatedBy = updatedBy;
        this.tenantId = tenantId;
        this.datasourceConfigId = datasourceConfigId;
        this.dbPrefix = dbPrefix;
    }

}
