package com.ctg.aatime.commons.enums;

/**
 * @author pjmike
 */
public enum ErrorMsgEnum {
    SEVER_TIMEOUT("服务器连接超时"),
    WEIXIN_SERVER_FAIL("微信服务器连接失败"),
    SEVER_FAIL("code无效,连接失败"),
    REDIS_VALUEISNULL("值为错误"),
    REDIS_EXPIRE("登录态过期");
    private String msg;

    ErrorMsgEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    }
