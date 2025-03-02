package cn.lakecode.web.utils;

public class TypeUtils {

    public static Integer toInt(String obj, Integer defaultVal) {
        if (obj == null) {
            return defaultVal;
        }
        try {
            return Integer.parseInt(obj);
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
