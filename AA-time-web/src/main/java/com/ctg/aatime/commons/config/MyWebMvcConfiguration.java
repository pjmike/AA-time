package com.ctg.aatime.commons.config;

import com.ctg.aatime.web.interceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author pjmike
 * @create 2018-03-22 21:17
 */
@Configuration
public class MyWebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private Interceptor interceptor;

    /**
     * 重写父类的拦截器方法
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截路径
        registry.addInterceptor(interceptor).addPathPatterns("/index");
    }

    /**
     * 重写父类的跨域请求接口
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //添加映射路径
        registry.addMapping("/**")
                //放行哪些原始域
                .allowedOrigins("*")
                //放行哪些请求头
                .allowedHeaders("*")
                //是否发送cookie信息
                .allowCredentials(true)
                //放行哪些请求方法
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                //暴露哪些请求头
                .exposedHeaders("Header1", "Header2");
    }
}
