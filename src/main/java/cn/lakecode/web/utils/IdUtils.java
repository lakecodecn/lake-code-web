package cn.lakecode.web.utils;

import java.util.UUID;

public class IdUtils {

    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
