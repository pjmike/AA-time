package com.ctg.aatime.commons.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 响应结果类
 *
 * @author pjmike
 * @create 2018-03-13 19:21
 */
@Data
@AllArgsConstructor
public class ResponseResult {
    private int code;
    private String msg;
    private Object data;
}
