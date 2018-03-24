package com.ctg.aatime.web.controller.wx;

import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.api.impl.WxQrCodeServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序二维码接口
 *
 * @author pjmike
 * @create 2018-03-20 17:12
 */
@RestController
@Slf4j
public class WxQrCodeController {
    private WxQrCodeServiceImpl service = new WxQrCodeServiceImpl();
    @GetMapping(value = "/code")
    public ResponseResult createCode() {
        //TODO 待重构
        String ImageUrl = service.createQrCodeImgUrl();
        return FormatResponseUtil.formatResponse(ImageUrl);
    }
}
