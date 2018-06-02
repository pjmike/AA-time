package com.ctg.api;

import java.io.File;
import java.util.*;
/**
 * @author pjmike
 * @create 2018-03-22 21:00
 */
public interface WxQrCodeService {
    /**
     * 获取微信小程序二维码
     * @return
     */
    File createQrCodeImgUrl(Map<String,Object> map);
}
