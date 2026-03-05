package top.tangyh.lamp.base.vo.save;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 表单保存方法VO
 * 角色视图映射配置表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:49:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode
@Builder
@Schema(description = "角色视图映射配置表")
public class RoleStatusMappingSaveVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 业务节点编码
     */
    @Schema(description = "业务节点编码")
    @NotEmpty(message = "请填写业务节点编码")
    @Size(max = 10, message = "业务节点编码长度不能超过{max}")
    private String nodeCode;
    /**
     * 角色编码:1-镇级网格专员;2-基层部门分管领导;3-基层部门负责人;4-基层部门专员
     */
    @Schema(description = "角色编码:1-镇级网格专员")
    @NotEmpty(message = "请填写角色编码:1-镇级网格专员")
    @Size(max = 10, message = "角色编码:1-镇级网格专员长度不能超过{max}")
    private String roleCode;
    /**
     * 展示状态
     */
    @Schema(description = "展示状态")
    @NotEmpty(message = "请填写展示状态")
    @Size(max = 50, message = "展示状态长度不能超过{max}")
    private String displayStatus;


}
