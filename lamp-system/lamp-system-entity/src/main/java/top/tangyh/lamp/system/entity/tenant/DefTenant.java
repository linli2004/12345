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
import top.tangyh.lamp.model.enumeration.system.TenantConnectTypeEnum;
import top.tangyh.lamp.system.enumeration.tenant.DefTenantRegisterTypeEnum;

import java.time.LocalDateTime;

import static top.tangyh.lamp.model.constant.Condition.LIKE;

/**
 * <p>
 * 实体类
 * 企业
 * </p>
 *
 * @author zuihou
 * @since 2021-10-27
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("def_tenant")
@AllArgsConstructor
public class DefTenant extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 企业编码
     */
    @TableField(value = "code", condition = LIKE)
    private String code;

    /**
     * 企业名称
     */
    @TableField(value = "name", condition = LIKE)
    private String name;

    /**
     * 企业简称
     */
    @TableField(value = "abbreviation", condition = LIKE)
    private String abbreviation;

    /**
     * 统一社会信用代码
     */
    @TableField(value = "credit_code", condition = LIKE)
    private String creditCode;

    /**
     * 联系人
     */
    @TableField(value = "contact_person", condition = LIKE)
    private String contactPerson;

    /**
     * 联系方式
     */
    @TableField(value = "contact_phone", condition = LIKE)
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @TableField(value = "contact_email", condition = LIKE)
    private String contactEmail;

    /**
     * 省
     */
    @TableField(value = "province_id")
    private Long provinceId;

    /**
     * 省
     */
    @TableField(value = "province_name", condition = LIKE)
    private String provinceName;

    /**
     * 市
     */
    @TableField(value = "city_id")
    private Long cityId;

    /**
     * 市
     */
    @TableField(value = "city_name", condition = LIKE)
    private String cityName;

    /**
     * 区
     */
    @TableField(value = "district_id")
    private Long districtId;

    /**
     * 区
     */
    @TableField(value = "district_name", condition = LIKE)
    private String districtName;

    /**
     * 详细地址
     */
    @TableField(value = "address", condition = LIKE)
    private String address;

    /**
     * 类型;#{CREATE:创建;REGISTER:注册}
     */
    @TableField(value = "register_type")
    private DefTenantRegisterTypeEnum registerType;

    /**
     * 数据源链接类型;#TenantConnectTypeEnum{LOCAL:本地;REMOTE:远程}
     */
    @TableField(value = "connect_type")
    private TenantConnectTypeEnum connectType;
    /** 类别 */
    @TableField(value = "classify")
    private String classify;

    /**
     * 状态;0-禁用 1-启用
     */
    @TableField(value = "state")
    private Boolean state;

    /**
     * 审核状态;[05-正常 10-待初始化 15-已撤回 20-待审核 25-已拒绝 30-已同意]
     *
     * @Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.System.TENANT_STATUS)
     */
    @TableField(value = "status")
    private String status;
    /**
     * 审核意见
     */
    @TableField(value = "review_comments", condition = LIKE)
    private String reviewComments;

    /**
     * 内置
     */
    @TableField(value = "readonly_")
    private Boolean readonly;

    /**
     * 创建人
     */
    @TableField(value = "created_name", condition = LIKE)
    private String createdName;

    /**
     * 有效期;
     * 为空表示永久
     */
    @TableField(value = "expiration_time")
    private LocalDateTime expirationTime;

    /**
     * 企业简介
     */
    @TableField(value = "describe_", condition = LIKE)
    private String describe;


    @Builder
    public DefTenant(Long id, LocalDateTime createdTime, Long createdBy, LocalDateTime updatedTime, Long updatedBy,
                     String code, String name, String abbreviation, String creditCode, String contactPerson,
                     String contactPhone, String contactEmail, Long provinceId, String provinceName, Long cityId, String cityName,
                     Long districtId, String districtName, String address, DefTenantRegisterTypeEnum registerType, TenantConnectTypeEnum connectType, Boolean state,
                     String status, Boolean readonly, String createdName, LocalDateTime expirationTime, String describe) {
        this.id = id;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.updatedTime = updatedTime;
        this.updatedBy = updatedBy;
        this.code = code;
        this.name = name;
        this.abbreviation = abbreviation;
        this.creditCode = creditCode;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.cityId = cityId;
        this.cityName = cityName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.address = address;
        this.registerType = registerType;
        this.connectType = connectType;
        this.state = state;
        this.status = status;
        this.readonly = readonly;
        this.createdName = createdName;
        this.expirationTime = expirationTime;
        this.describe = describe;
    }

}
