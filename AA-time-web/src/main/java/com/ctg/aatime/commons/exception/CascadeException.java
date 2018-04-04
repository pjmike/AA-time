package com.ctg.aatime.commons.exception;

/**
 * 级联失败异常
 * Created By Cx On 2018/4/4 12:54
 */
public class CascadeException extends RuntimeException {

    public CascadeException() {
    }

    public CascadeException(String message) {
        super(message);
    }

    public CascadeException(String message, Throwable cause) {
        super(message, cause);
    }
}
