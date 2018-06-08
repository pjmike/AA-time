package com.ctg.aatime.commons.config;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.BeanMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * 1.   websocket 配置类
 * 2.   @EnableWebSocketMessageBroker注解用于开启使用STOMP协议来传输基于代理（MessageBroker）的消息
 *
 * @author pjmike
 * @create 2018-03-24 11:57
 */
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 首先要连接stomp端点
     *
     * @param stompEndpointRegistry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //注册一个STOMP的endpoint端点，并指定使用SockJS协议,前端通过这个端点与服务器建立websocket连接
        stompEndpointRegistry.addEndpoint("/websocket")
                //解决跨域问题
                .setAllowedOrigins("*");
//                .withSockJS().setInterceptors(webSocketInterceptor());
    }

    /**
     * MessageBrokerRegistry,配置消息代理
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //设置消息代理,以/topic,/queue为前缀
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setUserDestinationPrefix("/user");
        //设置应用程序前缀，前端发起的请求会自动添加/app
//        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * 消息传输参数设置
     *
     * @param registry
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(8192)
                .setSendBufferSizeLimit(8192)
                .setSendTimeLimit(10000);
    }

    /**
     * 输入通道参数设置
     *
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(4)
                .maxPoolSize(8)
                .keepAliveSeconds(60);
        registration.setInterceptors(presenceChannelInterceptor());
    }

    /**
     * 输出参数设置
     *
     * @param registration
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(4).maxPoolSize(8);
        registration.setInterceptors(presenceChannelInterceptor());
    }

    @Bean
    public WebSocketInterceptor webSocketInterceptor() {
        return new WebSocketInterceptor();
    }

    @Bean
    public PresenceChannelInterceptor presenceChannelInterceptor() {
        return new PresenceChannelInterceptor();
    }
}
