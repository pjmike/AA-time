package com.ctg.aatime.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pjmike
 */
@AllArgsConstructor
public enum ErrorMsgEnum {
    /**
     * 服务器连接超时
     */
    INNER_SEVER_TIMEOUT("服务器连接超时"),
    /**
     * 服务器连接失败
     */
    SERVER_FAIL_CONNECT("服务器连接失败"),
    /**
     * session_key过期
     */
    REDIS_EXPIRE("session_key过期"),
    /**
     * redis值不存在
     */
    REDIS_VALUE_NULL("redis值不存在"),
    /**
     * 用户不存在
     */
    USER_NOT_FOUND("用户不存在");
    @Getter
    private String msg;
}
