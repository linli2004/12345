package top.tangyh.lamp.base.entity.common;

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
 * 个性参数
 * </p>
 *
 * @author zuihou
 * @since 2021-11-08
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("base_parameter")
@AllArgsConstructor
public class BaseParameter extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 参数键
     */
    @TableField(value = "key_", condition = LIKE)
    private String key;

    /**
     * 参数值
     */
    @TableField(value = "value", condition = LIKE)
    private String value;

    /**
     * 参数名称
     */
    @TableField(value = "name", condition = LIKE)
    private String name;

    /**
     * 备注
     */
    @TableField(value = "remarks", condition = LIKE)
    private String remarks;

    /**
     * 状态
     */
    @TableField(value = "state")
    private Boolean state;

    /**
     * 类型;[10-系统参数 20-业务参数]@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.System.PARAMETER_TYPE)
     */
    @TableField(value = "param_type", condition = LIKE)
    private String paramType;

    /**
     * 创建人组织
     */
    @TableField(value = "created_org_id")
    private Long createdOrgId;


    @Builder
    public BaseParameter(Long id, Long createdBy, LocalDateTime createdTime, Long updatedBy, LocalDateTime updatedTime,
                         String key, String value, String name, String remarks, Boolean state,
                         String paramType, Long createdOrgId) {
        this.id = id;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
        this.key = key;
        this.value = value;
        this.name = name;
        this.remarks = remarks;
        this.state = state;
        this.paramType = paramType;
        this.createdOrgId = createdOrgId;
    }

}
