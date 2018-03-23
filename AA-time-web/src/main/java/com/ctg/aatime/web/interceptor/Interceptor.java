package com.ctg.aatime.web.interceptor;


import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.exception.LoginException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //TODO 待重构
        if (StringUtils.equals(request.getServletPath(), "/login")) {
            String key = request.getHeader("3rd_session");
            //在redis去找值
            long nowExpireTime = redisTemplate.getExpire(key);
            if (nowExpireTime < 0) {
                throw new LoginException(ErrorMsgEnum.REDIS_EXPIRE.getMsg());
            } else {
                String openidAndKey = (String) redisTemplate.opsForValue().get(key);
                if (StringUtils.isBlank(openidAndKey)) {
                    throw new LoginException(ErrorMsgEnum.REDIS_VALUEISNULL.getMsg());
                }
                return true;
            }
        }
        return true;
    }
}
