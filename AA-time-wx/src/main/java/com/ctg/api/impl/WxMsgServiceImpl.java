package com.ctg.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.ctg.Bean.WxTemplateMessage;
import com.ctg.api.WxMsgService;
import com.ctg.config.WxProperties;
import com.ctg.exception.WxErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Objects;


/**
 * @author pjmike
 * @create 2018-04-19 23:40
 */
public class WxMsgServiceImpl implements WxMsgService{
    private RestTemplate restTemplate;

    public WxMsgServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private WxServiceImpl wxService = new WxServiceImpl(restTemplate);
    @Override
    public void sendTemplateMsg(WxTemplateMessage templateMessage) {
        String access_token = wxService.getAccessToken();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(WxProperties.TEMPLATE_MSG_SEND_URL, templateMessage.toString(), String.class, access_token);
        if (!Objects.equals(responseEntity.getStatusCode(), HttpStatus.OK)) {
            throw new WxErrorException("服务器错误");
        }
        String body = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(body);
        int errcode = jsonObject.getInteger("errcode");
        if (!Objects.equals(0, errcode)) {
            throw new WxErrorException("服务器错误");
        }
    }
}
