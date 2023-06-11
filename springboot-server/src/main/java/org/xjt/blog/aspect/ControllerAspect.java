package org.xjt.blog.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Controller层所有方法都加上切片
 */
@Slf4j
@Aspect
@Configuration
public class ControllerAspect {
    @Around("execution(* org.xjt.blog.controller.*.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取类名
        String className = point.getTarget().getClass().getSimpleName();
        // 获取方法名
        String methodName = point.getSignature().getName();
        // 获取方法的参数
        Object[] args = point.getArgs();

        // 获取controller的请求属性数据
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();

        printRequestLog(request, className, methodName, args);
        long start = System.currentTimeMillis();
        // 继续执行方法逻辑
        Object returnObj = point.proceed();
        printResponseLog(request, className, methodName, returnObj, System.currentTimeMillis() - start);

        return returnObj;
    }

    private void printRequestLog(HttpServletRequest request, String clazzName, String methodName, Object[] args) throws JsonProcessingException {
        log.debug("Request URL: [{}], URI: [{}], Request Method: [{}], IP: [{}]",
                request.getRequestURL(),
                request.getRequestURI(),
                request.getMethod(),
                ServletUtil.getClientIP(request));

        if (args == null || !log.isDebugEnabled()) {
            return;
        }

        boolean shouldNotLog = false;
        for (Object arg : args) {
            if (arg == null ||
                    arg instanceof HttpServletRequest ||
                    arg instanceof HttpServletResponse ||
                    arg instanceof MultipartFile ||
                    arg.getClass().isAssignableFrom(MultipartFile[].class)) {
                shouldNotLog = true;
                break;
            }
        }

        if (!shouldNotLog) {
            String requestBody = JSON.toJSONString(args);
            log.debug("{}.{} Parameters: [{}]", clazzName, methodName, requestBody);
        }
    }

    private void printResponseLog(HttpServletRequest request, String className, String methodName, Object returnObj, long usage) throws JsonProcessingException {
        if (log.isDebugEnabled()) {
            String returningData = null;
            if (returnObj != null) {
                if (returnObj.getClass().isAssignableFrom(byte[].class)) {
                    returningData = "Binary data";
                } else {
                    returningData = JSON.toJSONString(returnObj);
                }
            }
            log.debug("{}.{} Response: [{}], usage: [{}]ms", className, methodName, returningData, usage);
        }
    }
}
