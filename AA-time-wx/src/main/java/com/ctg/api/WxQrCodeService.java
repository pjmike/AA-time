package com.ctg.api;

/**
 * @author pjmike
 * @create 2018-03-22 21:00
 */
public interface WxQrCodeService {
    /**
     * 获取微信小程序二维码
     * @return
     */
    String createQrCodeImgUrl();
}
