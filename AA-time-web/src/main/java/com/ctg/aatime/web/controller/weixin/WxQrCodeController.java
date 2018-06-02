package com.ctg.aatime.web.controller.weixin;

import com.ctg.aatime.commons.qiniu.IQiNIuService;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.api.impl.WxQrCodeServiceImpl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.model.DefaultPutRet;
import com.sun.javafx.collections.MappingChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;
/**
 * 小程序二维码接口
 *
 * @author pjmike
 * @create 2018-03-20 17:12
 */
@RestController
@Slf4j
public class WxQrCodeController {
    private final WxQrCodeServiceImpl service = new WxQrCodeServiceImpl();
    @Value("${QiNiu}")
    private String QiNiu;
    @Autowired
    private IQiNIuService qiNIuService;
    private String fileName = "code.jpg";
    @GetMapping(value = "/code")
    public ResponseResult createCode(@RequestBody Map<String,Object> params) throws QiniuException {
        //TODO 重构
        File imageFile = service.createQrCodeImgUrl(params);
        Response response = qiNIuService.uploadFile(imageFile, fileName);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        log.info("filename: {}",putRet.key);
        return FormatResponseUtil.formatResponse(QiNiu+fileName);
    }
}
