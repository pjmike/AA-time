package com.ctg.aatime.domain.dto;

import lombok.Data;

/**
 * 获取小程序数据的封装类
 *
 * @author pjmike
 * @create 2018-03-29 19:45
 */
@Data
public class WxInfo {
    /**
     * 临时登录凭证code码
     */
    private String code;
    /**
     * 用户敏感信息加密数据
     */
    private String encryptedData;
    /**
     * 加密算法的初始向量
     */
    private String iv;
}
