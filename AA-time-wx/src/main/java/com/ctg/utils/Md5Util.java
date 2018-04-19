package com.ctg.utils;

import org.springframework.util.DigestUtils;

/**
 * md5生成器
 *
 * @author pjmike
 * @create 2018-02-04 19:32
 **/
public class Md5Util {
    private static String salt = "pjmike";
    public static String getMD5(String password) {
        String str = password + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
        return md5;
    }
}
