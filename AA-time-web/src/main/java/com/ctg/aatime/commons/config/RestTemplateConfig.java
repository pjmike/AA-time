package com.ctg.aatime.commons.config;

import com.ctg.aatime.commons.utils.WxMappingJackson2HttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author pjmike
 * @create 2018-03-20 17:17
 */
@Configuration
public class RestTemplateConfig {
    @Autowired
    private RestTemplateBuilder builder;
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = builder.build();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        return restTemplate;
    }
}
