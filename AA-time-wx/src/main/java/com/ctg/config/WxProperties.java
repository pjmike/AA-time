package com.ctg.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信相关配置
 *
 * @author pjmike
 * @create 2018-03-22 15:25
 */
@Data
public class WxProperties {
    /**
     * 小程序的appid
     */
    @Value("${WeiXin.appid}")
    private String appId;
    /**
     * 小程序的appSecret
     */
    @Value(("${WeiXin.secret}"))
    private String appSecret;
}
