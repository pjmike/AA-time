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
     * 获取access_token
     * @return
     */
    String getAccessToken();

    Map<String,Object> get(String jsCode);
}
