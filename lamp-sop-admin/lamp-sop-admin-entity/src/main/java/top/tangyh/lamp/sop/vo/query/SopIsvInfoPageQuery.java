package top.tangyh.lamp.sop.vo.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 表单查询条件VO
 * isv信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-11 10:51:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "isv信息表")
public class SopIsvInfoPageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    /**
     * appKey
     */
    @Schema(description = "appKey")
    private String appId;
    /**
     * 状态
     * [1-启用 2-禁用]
     */
    @Schema(description = "状态")
    private Integer status;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
    /**
     * 开始有效期
     */
    @Schema(description = "开始有效期")
    private LocalDateTime startExpirationTime;
    /**
     * 结束有效期
     */
    @Schema(description = "结束有效期")
    private LocalDateTime endExpirationTime;
    /**
     * 审核状态
     * [0-初始化 1-申请中 2-通过 99-退回]
     */
    @Schema(description = "审核状态")
    private Integer auditStatus;
    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    private LocalDateTime auditTime;
    /**
     * 提交时间
     */
    @Schema(description = "提交时间")
    private LocalDateTime submissionTime;
    /**
     * 创建方式
     * [0-后台创建 1-用户申请]
     */
    @Schema(description = "创建方式")
    private Integer creationMethod;
    /**
     * 审核意见
     */
    @Schema(description = "审核意见")
    private String reviewComments;
    /**
     * 租户id
     * def_tenant.id
     */
    @Schema(description = "租户id")
    private Long tenantId;
    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;
    /**
     * 回调接口
     */
    @Schema(description = "回调接口")
    private String notifyUrl;

}
