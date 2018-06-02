/*
package com.ctg.aatime.commons.config;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

*/
/**
 * @author pjmike
 * @create 2018-03-24 17:56
 *//*

public class WebSocketConfigRelay implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOrigins("*").withSockJS();
    }

    */
/**
     * 使用rabbitmq做消息代理
     *
     * @param registry
     *//*

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost("39.106.63.214")
                .setRelayPort(5672)
                .setClientLogin("guest")
                .setClientPasscode("guest");
    }
}
*/
