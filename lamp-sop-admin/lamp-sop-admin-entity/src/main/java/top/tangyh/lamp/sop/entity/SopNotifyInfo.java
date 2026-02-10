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
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.EQUAL;
import static top.tangyh.lamp.model.constant.Condition.LIKE;


/**
 * <p>
 * 实体类
 * 回调信息
 * </p>
 *
 * @author zuihou
 * @date 2025-12-17 15:38:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("sop_notify_info")
public class SopNotifyInfo extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * app_id
     */
    @TableField(value = "app_id", condition = LIKE)
    private String appId;
    /**
     * api_name
     */
    @TableField(value = "api_name", condition = LIKE)
    private String apiName;
    /**
     * api_version
     */
    @TableField(value = "api_version", condition = LIKE)
    private String apiVersion;
    /**
     * 回调url
     */
    @TableField(value = "notify_url", condition = LIKE)
    private String notifyUrl;
    /**
     * 最近一次发送时间
     */
    @TableField(value = "last_send_time", condition = EQUAL)
    private LocalDateTime lastSendTime;
    /**
     * 下一次发送时间
     */
    @TableField(value = "next_send_time", condition = EQUAL)
    private LocalDateTime nextSendTime;
    /**
     * 最大发送次数
     */
    @TableField(value = "send_max", condition = EQUAL)
    private Integer sendMax;
    /**
     * 已发送次数
     */
    @TableField(value = "send_cnt", condition = EQUAL)
    private Integer sendCnt;
    /**
     * 发送内容
     */
    @TableField(value = "content", condition = LIKE)
    private String content;
    /**
     * 状态
     * [0-待发送 1-发送成功,2-发送失败,3-重试结束]
     */
    @TableField(value = "notify_status", condition = EQUAL)
    private String notifyStatus;
    /**
     * 失败原因
     */
    @TableField(value = "error_msg", condition = LIKE)
    private String errorMsg;
    /**
     * 返回结果
     */
    @TableField(value = "result_content", condition = LIKE)
    private String resultContent;
    /**
     * 备注
     */
    @TableField(value = "remark", condition = LIKE)
    private String remark;
    /**
     * 租户ID
     */
    @TableField(value = "tenant_id", condition = EQUAL)
    private Long tenantId;


}
