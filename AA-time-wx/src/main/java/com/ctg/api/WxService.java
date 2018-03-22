package com.ctg.api;

import java.util.Map;

/**
 * 连接微信相关操作
 *
 * @author pjmike
 * @create 2018-03-22 15:01
 */
public interface WxService {
    /**
     * 获取access_token的地址
     */
    String urlToAccessToken = "https://api.weixin.qq.com/cgi-bin/token?grant_type={client_credential}&appid={APPID}&secret={APPSECRET}";
    /**
     * 获取sessionInfo地址
     */
    String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={appSecret}&js_code={jsCode}&grant_type=authorization_code";

    /**
     * 获取access_token
     * @return
     */
    String getAccessToken();

    Map<String,Object> get(String jsCode);
}
