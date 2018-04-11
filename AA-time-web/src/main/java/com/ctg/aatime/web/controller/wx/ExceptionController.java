package com.ctg.aatime.web.controller.wx;

import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.exception.CascadeException;
import com.ctg.aatime.commons.exception.LoginException;
import com.ctg.aatime.commons.exception.WeiXinException;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
/**
 * 自定义异常处理器
 *
 * @author pjmike
 * @create 2018-03-29 22:00
 */
@ControllerAdvice
public class ExceptionController {
    /**
     *
     *
     * @param w 出现的异常
     * @return
     */
    @ExceptionHandler(WeiXinException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult weiXinExceptionHandler(WeiXinException w) {
        return FormatResponseUtil.error(w.getMessage());
}
    /**
     * 登录过程中出现的异常
     *
     * @param w 出现的异常
     * @return
     */
    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult loginExceptionHandler(LoginException w) {
        return FormatResponseUtil.error(w.getMessage());
    }

    //TODO 这样写对吗？
    @ExceptionHandler(CascadeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult cascadeExceptionHandler(CascadeException c) {
        return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
    }
}
