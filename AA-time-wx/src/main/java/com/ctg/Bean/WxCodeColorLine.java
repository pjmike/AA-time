package com.ctg.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 小程序二维码颜色信息(rgb)
 *
 * @author pjmike
 * @create 2018-03-23 20:25
 */
@Data
@AllArgsConstructor
public class WxCodeColorLine {
    private String r = "0",g = "0", b = "0";
}
