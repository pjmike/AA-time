package com.ctg.api;

import com.ctg.Bean.WxTemplateMessage;

/**
 * 消息模板接口
 *
 * @author pjmike
 * @create 2018-04-19 23:25
 */
public interface WxMsgService {
    void sendTemplateMsg(WxTemplateMessage templateMessage);
}
