package com.gitee.sop.gateway.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 六如
 */
public class DateUtil {


    public static LocalDateTime parseToLocalDateTime(String date, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

}
