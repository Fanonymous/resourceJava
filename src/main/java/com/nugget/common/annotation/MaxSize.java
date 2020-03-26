package com.nugget.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MaxSize {

    public int max() default 20;

    public String message() default "字段超出最大限制长度";

}
