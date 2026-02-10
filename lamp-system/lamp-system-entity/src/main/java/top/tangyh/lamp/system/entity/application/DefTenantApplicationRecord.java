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

import static top.tangyh.lamp.model.constant.Condition.LIKE;

/**
 * <p>
 * 实体类
 * 租户应用授权记录
 * </p>
 *
 * @author zuihou
 * @since 2021-09-30
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("def_tenant_application_record")
@AllArgsConstructor
public class DefTenantApplicationRecord extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 授权ID
     */
    @TableField(value = "tenant_application_rel_id")
    private Long tenantApplicationRelId;

    /**
     * 企业ID
     */
    @TableField(value = "tenant_id")
    private Long tenantId;

    /**
     * 应用ID
     */
    @TableField(value = "application_id")
    private Long applicationId;

    /**
     * 应用名称
     */
    @TableField(value = "application_name", condition = LIKE)
    private String applicationName;

    /**
     * 企业名称
     */
    @TableField(value = "tenant_name", condition = LIKE)
    private String tenantName;

    /**
     * 操作人姓名
     */
    @TableField(value = "operate_by_name", condition = LIKE)
    private String operateByName;

    /**
     * 授权类型;[10-应用授权 20-应用续期 30-取消授权]
     *
     * @Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.System.APPLICATION_GRANT_TYPE)
     */
    @TableField(value = "grant_type", condition = LIKE)
    private String grantType;


    @Builder
    public DefTenantApplicationRecord(Long id, Long createdBy, LocalDateTime createdTime, Long updatedBy, LocalDateTime updatedTime,
                                      Long tenantApplicationRelId, Long tenantId, Long applicationId, String applicationName, String tenantName,
                                      String operateByName, String grantType) {
        this.id = id;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
        this.tenantApplicationRelId = tenantApplicationRelId;
        this.tenantId = tenantId;
        this.applicationId = applicationId;
        this.applicationName = applicationName;
        this.tenantName = tenantName;
        this.operateByName = operateByName;
        this.grantType = grantType;
    }

}
