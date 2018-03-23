package com.ctg.aatime.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码
 *
 * @author pjmike
 * @create 2018-03-23 16:51
 */
@AllArgsConstructor
public enum ResponseStatusEnum {
    /**
     * 请求成功返回0
     */
    SUCCESS(0),
    /**
     * 请求失败返回1
     */
    FAILED(1);
    @Getter
    private Integer code;

}
