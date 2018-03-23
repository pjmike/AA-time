package com.ctg.aatime.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pjmike
 */
@AllArgsConstructor
public enum ErrorMsgEnum {
    SEVER_TIMEOUT("服务器连接超时"),
    WEIXIN_SERVER_FAIL("微信服务器连接失败"),
    SEVER_FAIL("code无效,连接失败"),
    REDIS_VALUEISNULL("值为错误"),
    REDIS_EXPIRE("登录态过期");
    @Getter
    private String msg;
}
