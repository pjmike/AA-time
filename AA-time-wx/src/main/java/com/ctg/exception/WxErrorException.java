package com.ctg.exception;

import org.springframework.http.HttpStatus;

/**
 * @author pjmike
 * @create 2018-04-20 0:07
 */
public class WxErrorException extends RuntimeException{
    public WxErrorException() {
        super();
    }

    public WxErrorException(String message) {
        super(message);
    }

    public WxErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
