package com.nugget.common.aspect;

import com.nugget.common.annotation.NoRepeatSubmit;
import com.nugget.common.utils.DateUtils;
import com.nugget.common.utils.R;
import com.nugget.common.utils.RedisUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhaoyifan
 * @Date 2019/6/24 12:19
 * @Description TODO
 */
@Aspect
@Component
public class NoRepeatSubmitAspect {

    @Autowired
    private RedisUtils redisUtils;

    @Pointcut("@annotation(com.nugget.common.annotation.NoRepeatSubmit)")
    public void logPointCut() {

    }

    @Around("logPointCut() && @annotation(nrs)")
    public Object arround(ProceedingJoinPoint pjp, NoRepeatSubmit nrs) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String accesstoken = request.getHeader("accesstoken");
            String key = accesstoken + "_" + request.getServletPath();
                if (redisUtils.get(key) == null) {// 如果缓存中有这个url视为重复提交
                    redisUtils.set(key,"1",3);
                    Object o = pjp.proceed();
                    return o;
                } else {
                    return R.error(407,"");
                }
        } catch (Throwable e) {
            e.printStackTrace();
            return R.error();
        }

    }
}
