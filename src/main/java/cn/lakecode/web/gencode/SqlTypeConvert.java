package cn.lakecode.web.gencode;

import java.util.concurrent.ConcurrentHashMap;

public class SqlTypeConvert {

    private static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    static {
        map.put("tinyint", "java.lang.Integer");
        map.put("int", "java.lang.Integer");
        map.put("bigint", "java.lang.Long");
        map.put("double", "java.lang.Double");
        map.put("decimal", "java.math.BigDecimal");
        map.put("varchar", "java.lang.String");
        map.put("text", "java.lang.String");
        map.put("date", "java.time.LocalDate");
        map.put("datetime", "java.time.LocalDateTime");
    }

    public static String convert(String sqlType) {
        return map.getOrDefault(sqlType, "java.lang.String");
    }
}
