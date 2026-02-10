package top.tangyh.lamp.sop.entity;

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

import java.io.Serial;

import static com.baomidou.mybatisplus.annotation.SqlCondition.EQUAL;
import static top.tangyh.lamp.model.constant.Condition.LIKE;


/**
 * <p>
 * 实体类
 * 接口信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-11 10:34:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("sop_api_info")
public class SopApiInfo extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 应用名称
     */
    @TableField(value = "application", condition = LIKE)
    private String application;
    /**
     * 接口名称
     */
    @TableField(value = "api_name", condition = LIKE)
    private String apiName;
    /**
     * 版本号
     */
    @TableField(value = "api_version", condition = LIKE)
    private String apiVersion;
    /**
     * 接口描述
     */
    @TableField(value = "description", condition = LIKE)
    private String description;
    /**
     * 备注
     */
    @TableField(value = "remark", condition = LIKE)
    private String remark;
    /**
     * 接口class
     */
    @TableField(value = "interface_class_name", condition = LIKE)
    private String interfaceClassName;
    /**
     * 方法名称
     */
    @TableField(value = "method_name", condition = LIKE)
    private String methodName;
    /**
     * 参数信息
     */
    @TableField(value = "param_info", condition = LIKE)
    private String paramInfo;
    /**
     * 接口是否需要授权访问
     */
    @TableField(value = "is_permission", condition = EQUAL)
    private Integer isPermission;
    /**
     * 是否需要appAuthToken
     */
    @TableField(value = "is_need_token", condition = EQUAL)
    private Integer isNeedToken;
    /**
     * 是否有公共响应参数
     */
    @TableField(value = "has_common_response", condition = EQUAL)
    private Integer hasCommonResponse;
    /**
     * 注册来源
     * [1-系统注册 2-手动注册]
     */
    @TableField(value = "reg_source", condition = EQUAL)
    private Integer regSource;
    /**
     * 接口模式
     * [1-open接口 2-Restful模式]
     */
    @TableField(value = "api_mode", condition = EQUAL)
    private Integer apiMode;
    /**
     * 状态
     * [1-启用 0-禁用]
     */
    @TableField(value = "status", condition = EQUAL)
    private Integer status;


}
