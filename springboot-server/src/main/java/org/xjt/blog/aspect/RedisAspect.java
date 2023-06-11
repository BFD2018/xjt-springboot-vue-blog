package org.xjt.blog.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.xjt.blog.exception.XiongException;

/**
 * Redis切面处理类
 *
 * @author Xiong
 */
//@Aspect
//@Configuration
public class RedisAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());
    //是否开启redis缓存  true开启   false关闭
    @Value("${spring.redis.open: false}")
    private boolean open;

    /**
     * 所有的redis操作前后 都加上环绕通知，有异常时logger打印异常信息
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* org.xjt.blog.utils.RedisUtils.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        if(open){
            try{
                result = point.proceed();
            }catch (Exception e){
                logger.error("redis error", e);
                throw new XiongException("Redis服务异常");
            }
        }
        return result;
    }
}

