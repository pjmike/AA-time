package com.ctg.config;


import java.util.HashMap;
import java.util.Map;

/**
 * 二维码参数设置
 *
 * @author pjmike
 * @create 2018-03-23 18:13
 */
public class WxQrCodeProperties {
    /**
     * 微信二维码的路径
     */
    public static final String PATH = "/";
    /**
     * 二维码尺寸
     */
    public static final Integer WIDTH = 430;

    /**
     * 是否自动调色
     */
    public static final boolean AUTO_COLOR = false;
    /**
     * rgb颜色
     */
    public static final String LINE_COLOR = "";
    /**
     * 二维码参数
     */
    public static final Map<String, Object> QRCODEPARMS = new HashMap<>(16);

    /**
     * 初始化二维码参数
     */
    static {
        QRCODEPARMS.put("path", PATH);
        QRCODEPARMS.put("auto_color", AUTO_COLOR);
        QRCODEPARMS.put("width", WIDTH);
        QRCODEPARMS.put("line_color", LINE_COLOR);
    }
}
