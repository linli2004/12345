package top.tangyh.lamp.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.Accessors;
import top.tangyh.basic.base.entity.Entity;

import java.io.Serial;



@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@TableName("work_read_record")
public class WorkReadRecord extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "readKey 不允许为空")
    private String readKey;
    @NotBlank(message = "roleCode 不允许为空")
    private String roleCode;
    @NotBlank(message = "readRandomStr 不允许为空")
    private String readRandomStr;

    @TableField(exist = false)
    private Boolean readStatus;
}
