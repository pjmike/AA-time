package com.ctg.aatime.commons.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * 监听订阅地址的用户
 *
 * @author pjmike
 * @create 2018-06-05 18:40
 */
@Component
@Slf4j
public class StompSubcribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
        //这里sessionId对应拦截器中存放的key
        log.info("stomp subscribe : {}", accessor.getMessageHeaders());
    }
}
