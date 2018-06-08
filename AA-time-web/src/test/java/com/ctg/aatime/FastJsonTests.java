package com.ctg.aatime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
/**
 * @author pjmike
 * @create 2018-06-07 15:48
 */
public class FastJsonTests {
    public static void main(String[] args) {
        Map<Long, Long> map = new HashMap<>();
        map.put(23423424334L, 234243234L);
        System.out.println(JSON.toJSON(map));

    }
}
