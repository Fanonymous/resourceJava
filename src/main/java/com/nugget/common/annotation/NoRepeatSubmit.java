package com.nugget.common.annotation;

import org.aspectj.lang.annotation.Aspect;

import java.lang.annotation.*;

/**
 * @功能描述 防止重复提交
 * @author zhaoyifan
 * @date 2019-06-24
 */
@Target(ElementType.METHOD) // 作用到方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
@Documented
public @interface NoRepeatSubmit {



}
