package com.ctg.aatime.web.controller.wx;

import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.api.impl.WxQrCodeServiceImpl;
import com.ctg.api.impl.WxServiceImpl;
import com.ctg.utils.ImgUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序二维码接口
 *
 * @author pjmike
 * @create 2018-03-20 17:12
 */
@RestController
public class WxQrCodeController {
    private WxQrCodeServiceImpl service;
    @GetMapping(value = "/code")
    public ResponseResult createCode() {
        //TODO 待重构
        String ImageUrl = service.createQrCodeImgUrl();
        return new ResponseResult(0, "成功", ImageUrl);
    }
}
