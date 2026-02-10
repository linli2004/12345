package com.gitee.sop.gateway.util;

/**
 * @author 六如
 */
public class FieldUtil {

    private static final String REGEX = "([a-z])([A-Z])";
    private static final String REGEX_VAL = "$1_$2";
    private static final char UNDERLINE = '_';


    /**
     * 驼峰转下划线
     *
     * @return 返回下划线
     */
    public static String camelCaseToSnakeCase(String name) {
        return name.replaceAll(REGEX, REGEX_VAL).toLowerCase();
    }

    /**
     * 下划线转驼峰
     *
     * @param param 内容
     * @return 返回转换后的字符串
     */
    public static String snakeCaseToCamelCase(String param) {
        if (param == null || param.trim().isEmpty()) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
