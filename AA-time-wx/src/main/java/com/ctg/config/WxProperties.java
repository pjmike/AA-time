package com.ctg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信相关配置
 * 静态变量初始化顺序按声明的先后来执行
 *
 * @author pjmike
 * @create 2018-03-22 15:25
 */
public class WxProperties {
    /**
     * 获取access_token的地址
     */
    public static final String URLTOACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={appSecret}";
    /**
     * 获取session_info地址
     */
    public static final String URL2SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={appSecret}&js_code={jsCode}&grant_type=authorization_code";

    /**
     * 获取二维码的地址
     */
    public static final String CODEURL = "https://api.weixin.qq.com/wxa/getwxacode?access_token=";

    public static final String TEMPLATE_MSG_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";
    /**
     * 小程序的appid
     */
    public static final String APPID = "wx2673278f5838ab8b";
    /**
     * 小程序的appSecret
     */
    public static final String APPSECRET = "f491576e6a1221b06f78c76f1f446098";
    /**
     * grant_type
     */
    public static final String GRANT_TYPE = "client_credential";
    /**
     * 获取access_token所需参数map
     */
    public static final Map<String, String> ACCESSTOKEN_VARS = new HashMap<>(16);
    /**
     * 获取openid，session_key所需参数map
     */
    public static final Map<String, String> SESSION_INFO = new HashMap<>(16);

    /**
     * 静态初始化 ACCESSTOKEN_VARS
     */
    static {
        ACCESSTOKEN_VARS.put("appid", APPID);
        ACCESSTOKEN_VARS.put("appSecret", APPSECRET);
    }

    /**
     * 静态初始化 SESSION_INFO
     */
    static {
        SESSION_INFO.put("appid", APPID);
        SESSION_INFO.put("appSecret", APPSECRET);
    }

}
