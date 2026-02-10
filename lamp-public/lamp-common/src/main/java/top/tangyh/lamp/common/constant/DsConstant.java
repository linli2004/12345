package top.tangyh.lamp.common.constant;

import static top.tangyh.basic.context.ContextConstants.TENANT_BASE_POOL_NAME_HEADER;
import static top.tangyh.basic.context.ContextConstants.TENANT_EXTEND_POOL_NAME_HEADER;

/**
 * @author tangyh
 * @version v1.0
 * @date 2021/9/15 6:05 下午
 * @create [2021/9/15 6:05 下午 ] [tangyh] [初始创建]
 */
public interface DsConstant {
    /**
     * 默认数据源
     */
    String DEFAULTS = "0";
    /**
     * 动态租户数据源
     */
    String BASE_TENANT = "#thread." + TENANT_BASE_POOL_NAME_HEADER;
    String EXTEND_TENANT = "#thread." + TENANT_EXTEND_POOL_NAME_HEADER;
}
