package com.nugget.config;

import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * Created by LIUHAO
 * Date:2019/5/7
 */
public class MybatisPlusOptLockerConfig {
 @Bean
 public PerformanceInterceptor performanceInterceptor(){
  return new PerformanceInterceptor();
 }

 @Bean
 public OptimisticLockerInterceptor optimisticLockerInterceptor() {
  return new OptimisticLockerInterceptor();
 }
}
