package com.ctg.aatime.commons.config;

import com.ctg.aatime.commons.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * websocket握手接口
 *
 * @author pjmike
 * @create 2018-06-05 11:29
 */
@Component
@Slf4j
public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        //解决The extension [x-webkit-deflate-frame] is not supported问题
        if (request.getHeaders().containsKey("Sec-WebSocket-Extensions")) {
            request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
        }
        //检查session的值是否存在
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            HttpSession session = serverHttpRequest.getServletRequest().getSession(false);
            if (session != null) {
                String userId = (String) session.getAttribute("userId");
                log.info("userId : {}",userId);
                if (StringUtils.isEmpty(userId)) {
                    userId = "USERID_IS_NULL";
                }
                //把session和userId存放起来
                attributes.put(Constants.SESSIONID, session.getId());
                attributes.put(Constants.KEY_USER_ID, userId);
            }
        }
        return true;
    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
//        super.afterHandshake(request, response, wsHandler, ex);
//    }
}
