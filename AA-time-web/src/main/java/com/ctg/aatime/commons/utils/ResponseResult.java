package com.ctg.aatime.commons.utils;

import lombok.Data;

/**
 * 响应结果类
 *
 * @author pjmike
 * @create 2018-03-13 19:21
 */
@Data
public class ResponseResult {
    /**
     * 返回码
     */
    private int code;
    /**
     * 返回提示信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private Object data;

    public ResponseResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(int code) {
        this.code = code;
    }
}
