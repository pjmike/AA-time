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
     * 获取access_token信息
     * @return
     */
    String getAccessToken();

    /**
     * 获取session_info信息
     *
     * @param jsCode
     * @return
     */
    Map<String,Object> getSessionInfo(String jsCode);

    Map<String, Object> getUserInfo(String data, String key, String iv, String encodingFormat) throws Exception;
}
