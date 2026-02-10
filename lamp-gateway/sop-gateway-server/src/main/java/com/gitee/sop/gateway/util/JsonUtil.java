package com.gitee.sop.gateway.util;

import com.alibaba.fastjson2.JSON;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

/**
 * json工具类,默认用fastjson2实现
 *
 * @author 六如
 */
public class JsonUtil {

    public static String toJSONString(Object object) {
        if (object == null) {
            return "null";
        }
        return JSON.toJSONString(object);
    }

    public static <T> T parseObject(String value, Class<T> clazz) {
        if (Objects.equals(value, "null") || ObjectUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, clazz);
    }

    public static <T> List<T> parseArray(String value, Class<T> clazz) {
        if (Objects.equals(value, "null") || ObjectUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseArray(value, clazz);
    }


}
