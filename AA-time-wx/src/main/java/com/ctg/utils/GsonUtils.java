package com.ctg.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * @author pjmike
 * @create 2018-03-22 15:51
 */
public class GsonUtils {
    static Gson gson = new Gson();

    /**
     * 将字符串解析成Map
     * @param string
     * @return
     */
    public static Map<String,Object> getGsonData(String string) {
        return gson.fromJson(string, new TypeToken<Map<String,Object>>(){}.getType());
    }
}
