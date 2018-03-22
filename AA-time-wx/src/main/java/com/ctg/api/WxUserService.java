package com.ctg.api;

/**
 * 用户登录相关操作
 *
 * @author pjmike
 * @create 2018-03-21 19:19
 */
public interface WxUserService {
    /**
     * 获取登录后的session信息
     *
     * @param jsCode
     * @return
     */
    String getSessionInfo(String jsCode);
}
