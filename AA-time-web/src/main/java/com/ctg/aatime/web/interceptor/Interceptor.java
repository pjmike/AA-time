package com.ctg.aatime.web.interceptor;


import com.ctg.aatime.commons.exception.LoginException;
import com.ctg.aatime.commons.utils.RedisOperator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 *
 * @author pjmike
 * @create 2018-03-13 20:41
 */
@Component
public class Interceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RedisOperator redisOperator;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String key = request.getHeader("3rd_session");
        if (StringUtils.isEmpty(key)) {
            throw new LoginException("用户未登陆，请及时登陆");
        }
        if (redisOperator.get(key) == null) {
            throw new LoginException("用户身份过期,请重新登录");
        }
        return true;
    }
}
