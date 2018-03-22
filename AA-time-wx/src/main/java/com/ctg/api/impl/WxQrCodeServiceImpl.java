package com.ctg.api.impl;

import com.ctg.api.WxQrCodeService;
import com.ctg.utils.ImgUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pjmike
 * @create 2018-03-22 21:02
 */
public class WxQrCodeServiceImpl implements WxQrCodeService{

    @Autowired
    private RestTemplate restTemplate;
    @Value(("${Image.savePath}"))
    private String ImageSavePath;
    @Value("{Image.url}")
    private String ImageUrl;
    private WxServiceImpl service;
    private String path = "/path";
    private String imageName = "code.jpg";
    @Override
    public String createQrCodeImgUrl() {
        //获取access_token
        String access_token = service.getAccessToken();
        //获取二维码的地址
        String url = codeUrl + access_token;
        Map<String, String> colors = new HashMap<>();
        colors.put("r", "0");
        colors.put("g", "0");
        colors.put("b", "0");
        Map<String, Object> map = new HashMap<>(16);
        map.put("path", path);
        map.put("width", 430);
        map.put("auto_color", false);
        map.put("line_color",colors);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);
        ResponseEntity<byte[]> responseEntity =  restTemplate.exchange(url, HttpMethod.POST, httpEntity, byte[].class);
        InputStream inputStream = new ByteArrayInputStream(responseEntity.getBody());
        ImgUtils.saveToImgByInputStream(inputStream, ImageSavePath, imageName);
        return ImageUrl+imageName;
    }
}
