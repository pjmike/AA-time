package com.ctg.api.impl;

import com.ctg.api.WxQrCodeService;
import com.ctg.config.WxProperties;
import com.ctg.config.WxQrCodeProperties;
import com.ctg.utils.ImgUtils;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
/**
 * @author pjmike
 * @create 2018-03-22 21:02
 */
public class WxQrCodeServiceImpl implements WxQrCodeService{

    private  RestTemplate restTemplate;

    private String imageName = "code.jpg";
    public WxQrCodeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private WxServiceImpl service = new WxServiceImpl(restTemplate);

    @Override
    public File createQrCodeImgUrl(Map<String,Object> map) {
        //获取access_token
        String access_token = service.getAccessToken(restTemplate);
        //获取二维码的地址
        String url = WxProperties.CODEURL + access_token;
        Map<String, Object> target = WxQrCodeProperties.QRCODEPARMS;
        target.put("path", map.get("path"));
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(target);
        byte[] bytes = restTemplate.postForObject(url, httpEntity, byte[].class);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        File file = ImgUtils.saveToImgByInputStream(inputStream, imageName);
        return file;
    }
}
