package com.ctg.aatime.web.controller.ws;

import com.ctg.aatime.commons.bean.ServerResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * websocket请求控制器
 *
 * @author pjmike
 * @create 2018-03-24 12:21
 */
@Controller
public class WsController {

    @Autowired
    private SimpMessagingTemplate template;
    /**
     * TODO 待重构
     */
    @MessageMapping("/welcome")
    public void sendMessageToAll(ServerResponseMessage message) {
        template.convertAndSend("/topic/notice",message.getResponseMessage());
    }
}
