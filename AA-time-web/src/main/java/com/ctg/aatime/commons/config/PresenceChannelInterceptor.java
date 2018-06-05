package com.ctg.aatime.commons.config;

import com.ctg.aatime.commons.utils.Constants;
import com.ctg.aatime.commons.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;

/**
 * stomp连接处理类
 *
 * @author pjmike
 * @create 2018-06-05 18:28
 */
@Component
@Slf4j
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {
    @Autowired
    private RedisOperator redisOperator;

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == null) {
            return;
        }
        String sessionId = accessor.getSessionAttributes().get(Constants.SESSIONID).toString();
        String userId = accessor.getSessionAttributes().get(Constants.KEY_USER_ID).toString();
        //判断客户端的连接状态
        switch (accessor.getCommand()) {
            case CONNECT:
                connect(sessionId, userId);
                break;
            case CONNECTED:
                break;
            case DISCONNECT:
                disconnect(sessionId, userId, accessor);
                break;
            default:
                break;

        }
    }

    private void connect(String sessionId, String userId) {
        log.info("stomp connect sessionId:{}", sessionId);
        //存放在redis
        String cacheName = Constants.WEBSOCKET_ACCOUNT;
        redisOperator.set(cacheName+userId, userId);
    }

    private void disconnect(String sessionId, String userId, StompHeaderAccessor accessor) {
        log.info("stomp disconnect sessionId： {}", sessionId);
        accessor.getSessionAttributes().remove(Constants.SESSIONID);
        accessor.getSessionAttributes().remove(Constants.KEY_USER_ID);
        String cacheName = Constants.WEBSOCKET_ACCOUNT;
        if (redisOperator.get(cacheName+userId) != null) {
            log.info("====清空缓存====");
            redisOperator.remove(cacheName+userId);
        }
    }
}
