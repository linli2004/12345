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


/**
 * <p>
 * 表单查询条件VO
 * 系统配置表
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
@EqualsAndHashCode
@Builder
@Schema(description = "系统配置表")
public class SopSysConfigPageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    /**
     * 配置key
     */
    @Schema(description = "配置key")
    private String configKey;
    /**
     * 配置值
     */
    @Schema(description = "配置值")
    private String configValue;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}
