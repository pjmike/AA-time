package com.ctg.aatime.commons.qiniu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 接受自定义参数
 *
 * @author pjmike
 * @create 2018-05-02 16:31
 */
@Component
@Data
@ConfigurationProperties(prefix = "qiniu")
public class QiNiuProperties {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String cdnPrefix;
}
