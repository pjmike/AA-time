package com.ctg.api.impl;

import com.ctg.api.WxService;
import com.ctg.config.WxProperties;
import com.ctg.utils.AesCbcUtil;
import com.ctg.utils.GsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author pjmike
 * @create 2018-03-22 15:35
 */
public class WxServiceImpl implements WxService {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getAccessToken() {
        Map result = restTemplate.getForObject(WxProperties.URLTOACCESSTOKEN, Map.class,WxProperties.ACCESSTOKEN_VARS);
        if (!StringUtils.isBlank((String) result.get("access_token"))) {
            //TODO
        }
        String access_token = (String) result.get("access_token");
        return access_token;
    }
    @Override
    public Map<String, Object> getSessionInfo(String jsCode) {
        //参数设置
        Map<String, String> params = WxProperties.SESSION_INFO;
        params.put("jsCode", jsCode);
        Map<String, Object> result = restTemplate.getForObject(WxProperties.URL2SESSION, Map.class, params);
        //返回map结果
        return result;
    }

    @Override
    public Map<String, Object> getUserInfo(String data, String key, String iv, String encodingFormat) throws Exception {
        //encryptedData加密数据进行AES解密
        String result = AesCbcUtil.decrypt(data, key, iv, encodingFormat);
        //将解密后的string字符串利用gson工具类转换为Map
        Map<String, Object> map = GsonUtils.getGsonData(result);
        return map;
    }
}
