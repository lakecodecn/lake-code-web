package cn.lakecode.web.utils;

import com.alibaba.fastjson2.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    /**
     * 获取第一个
     *
     * @param array json array
     * @return string
     */
    public static String first(String array) {
        try {
            List<String> list = JSONArray.parseArray(array, String.class);
            return list.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Long> toLongList(String array) {
        try {
            String[] split = array.split("[, |;]");
            return Arrays.stream(split).map(Long::parseLong).toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<Integer> toIntegerList(String array) {
        try {
            String[] split = array.split("[, |;]");
            return Arrays.stream(split).map(Integer::parseInt).toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
