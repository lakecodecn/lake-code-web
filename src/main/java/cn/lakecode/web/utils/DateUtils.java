package cn.lakecode.web.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {


    private final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return dateTime.format(formatter);
    }

    public static LocalDateTime parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(date, formatter);
    }

    /**
     * 获取剩余秒数
     *
     * @return sec
     */
    public static Long nextDaySec() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDay = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 1, 0, 0, 0);
        ZoneOffset offset = ZonedDateTime.now().getOffset();
        return nextDay.toEpochSecond(offset) - now.toEpochSecond(offset);
    }

}
