package top.tangyh.lamp.system.vo.save.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 租户连接
 *
 * @author zuihou
 * @date 2020/8/25 上午8:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@Schema(title = "DefTenantServiceInitVO", description = "初始化租户服务")
public class DefTenantServiceInitVO {
    /**
     * 数据源id
     */
    private Long datasourceConfigId;

    /**
     * 库前缀
     */
    private String database;
}
