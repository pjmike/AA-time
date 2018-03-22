package com.ctg.api;

/**
 * @author pjmike
 * @create 2018-03-22 21:00
 */
public interface WxQrCodeService {
    /**
     * 获取二维码的地址
     */
    String codeUrl = "https://api.weixin.qq.com/wxa/getwxacode?access_token=";
    /**
     * 获取微信小程序二维码
     * @return
     */
    String createQrCodeImgUrl();
}
