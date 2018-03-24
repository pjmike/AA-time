package com.ctg.aatime.commons.bean;

import lombok.Data;

/**
 * 服务器向浏览器发送消息类
 *
 * @author pjmike
 * @create 2018-03-24 12:18
 */
@Data
public class ServerResponseMessage {
    /**
     * 消息内容
     */
    private String responseMessage;
}
