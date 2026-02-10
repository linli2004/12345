package top.tangyh.lamp.sop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zuihou
 */
@AllArgsConstructor
@Getter
public enum ConfigKeyEnum {
    JWT_TIMEOUT_DAYS("admin.jwt-timeout-days", "365"),
    JWT_SECRET("admin.jwt.secret", ""),
    TORNA_SERVER_ADDR("admin.torna-server-addr", ""),
    OPEN_PROD_URL("admin.open-prod-url", ""),
    OPEN_SANDBOX_URL("admin.open-sandbox-url", "");

    private final String key;

    private final String defaultValue;


}
