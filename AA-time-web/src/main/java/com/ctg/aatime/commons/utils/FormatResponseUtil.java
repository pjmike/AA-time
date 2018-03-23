package com.ctg.aatime.commons.utils;

import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.enums.ResponseStatusEnum;

/**
 * 响应结果工具类
 *
 * @author pjmike
 * @create 2018-03-23 17:07
 */
public class FormatResponseUtil {
    /**
     * 请求成功，不携带数据
     *
     * @return
     */
    public static ResponseResult formatResponse() {
        ResponseResult result = null;
        return formatResponse(result);
    }

    public static ResponseResult formatResponse(ResponseResult result) {
        if (result == null) {
            result = new ResponseResult(ResponseStatusEnum.SUCCESS.getCode(), "请求成功", null);
        }
        return result;
    }

    /**
     * 请求成功,带数据
     *
     * @param object
     * @return
     */
    public static ResponseResult formatResponse(Object object) {
        return new ResponseResult(ResponseStatusEnum.SUCCESS.getCode(), "请求成功", object);
    }

    /**
     * 请求成功，携带提示信息和数据
     *
     * @param msg
     * @param object
     * @return
     */
    public static ResponseResult formatResponse(String msg, Object object) {
        return new ResponseResult(ResponseStatusEnum.SUCCESS.getCode(), msg, object);
    }

    /**
     * 请求成功,携带数据
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> ResponseResult formatResponseDomain(T t) {
        ResponseResult baseResult = new ResponseResult(0,"请求成功",t);
        return baseResult;
    }
    /**
     * 请求失败，返回错误码和错误信息
     *
     * @param errorMsgEnum
     * @return
     */
    public static ResponseResult error(ErrorMsgEnum errorMsgEnum) {
        return new ResponseResult(ResponseStatusEnum.FAILED.getCode(), errorMsgEnum.getMsg());
    }
}
