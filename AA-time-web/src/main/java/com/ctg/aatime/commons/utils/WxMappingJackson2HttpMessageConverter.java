package com.ctg.aatime.commons.utils;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * text/plain转换器
 *  微信接口文档虽说返回的是 Json 数据，但是同时返回的 Header 里面的 Content-Type 值确是 text/plain 的
 *  导致 RestTemplate 把数据从 HttpResponse 转换成 Object 的时候，找不到合适的 HttpMessageConverter 来转换
 *  所以添加一个转换器来支持text/plain
 * @author pjmike
 * @create 2018-06-05 15:42
 */
public class WxMappingJackson2HttpMessageConverter  extends MappingJackson2HttpMessageConverter {
    public WxMappingJackson2HttpMessageConverter() {
        //在构造函数设置支持text/plain
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        setSupportedMediaTypes(mediaTypes);
    }
}
