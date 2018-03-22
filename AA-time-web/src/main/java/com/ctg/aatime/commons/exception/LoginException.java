package com.ctg.aatime.commons.exception;

/**
 * 登录过程中的错误
 *
 * @author pjmike
 * @create 2018-03-14 19:48
 */
public class LoginException extends RuntimeException{

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }
}
