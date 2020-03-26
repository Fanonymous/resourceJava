package com.nugget.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            //放行哪些原始域
            .allowedOrigins("*")
             //是否发送Cookie信息
            .allowCredentials(true)
            //放行哪些原始域(头部信息)
            .allowedHeaders("*")
            //放行哪些原始域(请求方式)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .maxAge(3600);
    }
}
