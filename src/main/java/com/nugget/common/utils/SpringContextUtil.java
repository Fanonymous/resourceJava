package com.nugget.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Created by LIUHAO
 * Date:2019/6/24
 */
@Component
@Order(Integer.MIN_VALUE)
public class SpringContextUtil  implements ApplicationContextAware {

 private static ApplicationContext context ;

 /* (non Javadoc)
  * @Title: setApplicationContext
  * @Title: setApplicationContext
  * @Description: spring获取bean工具类
  * @param applicationContext
  * @throws BeansException
  * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
  */
 @Override
 public void setApplicationContext(ApplicationContext applicationContext)
   throws BeansException {
  System.out.println("applicationContext正在初始化,application:"+applicationContext);
  this.context = applicationContext;
 }

 /// 获取当前环境
 public static String getActiveProfile() {
  return context.getEnvironment().getActiveProfiles()[0];
 }


}
