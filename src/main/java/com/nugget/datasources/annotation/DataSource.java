package com.nugget.datasources.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 * @author hb
 * @email 1247898199@qq.com
 * @date 2019-01-09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String name() default "";
}
