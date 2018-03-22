package com.ctg.api.impl;

//import com.ctg.aatime.commons.enums.ErrorMsgEnum;
//import com.ctg.aatime.commons.exception.WeiXinException;
import com.ctg.api.WxService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pjmike
 * @create 2018-03-22 15:35
 */
@ConfigurationProperties(prefix = "wx")
public class WxServiceImpl implements WxService {

    /**
     * 小程序的appid
     */
    String appid;
    /**
     * 小程序的appSecret
     */
    String appSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getAccessToken() {
        Map<String, String> vars = new HashMap<>(16);
        vars.put("grant_type", "client_credential");
        vars.put("APPID", appid);
        vars.put("APPSECRET", appSecret);
        Map result = restTemplate.getForObject(urlToAccessToken, Map.class,vars);
        if (!StringUtils.isBlank((String) result.get("access_token"))) {
//            throw new WeiXinException(ErrorMsgEnum.WEIXIN_SERVER_FAIL.getMsg());
        }
        String access_token = (String) result.get("access_token");
        return access_token;
    }

    @Override
    public Map<String, Object> get(String jsCode) {
        //参数设置
        Map<String, String> params = new HashMap<>(16);
        params.put("appid", appid);
        params.put("appSecret", appSecret);
        params.put("jsCode", jsCode);
        Map<String, Object> result = restTemplate.getForObject(url, Map.class, params);
        //返回map结果
        return result;
    }
}
