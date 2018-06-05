package com.ctg.aatime.web.controller.weixin;

import com.ctg.Bean.WxTemplateMessage;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.api.WxMsgService;
import com.ctg.api.impl.WxMsgServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 模板消息
 *
 * @author pjmike
 * @create 2018-06-04 14:47
 */
@RestController
public class WxMsgController {
    @Autowired
    private RestTemplate template;
    private WxMsgService wxMsgService = new WxMsgServiceImpl(template);

    /**
     * 模板消息
     *
     * @return
     */
    @PostMapping("/message")
    public ResponseResult sendMessage(@RequestBody WxTemplateMessage message) {
        wxMsgService.sendTemplateMsg(message);
        return null;
    }
}
