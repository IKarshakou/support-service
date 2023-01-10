package com.training.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    private static final String LOG_MESSAGE = "Class [{}]: User [{}] called method [{}] with args [{}] and result is: ";
    private static final String METHOD_RETURN_VOID = "void";
    private static final String METHOD_RETURN_VALUE = "{}";

    @Around("Pointcuts.allServiceMethods()")
    public Object aroundServiceMethodsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        var username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        var methodSignature = (MethodSignature) joinPoint.getSignature();
        var targetClassName = joinPoint.getTarget().getClass().getName();
        var sc = joinPoint.getSignature().getDeclaringType().getName();
        
        var result = joinPoint.proceed();
        if (result == null) {
            log.info(LOG_MESSAGE + METHOD_RETURN_VOID,
                    targetClassName,
                    username,
                    methodSignature.getName(),
                    Arrays.toString(methodSignature.getParameterNames()));
        } else {
            log.info(LOG_MESSAGE + METHOD_RETURN_VALUE,
                    targetClassName,
                    username,
                    methodSignature.getName(),
                    Arrays.toString(methodSignature.getParameterNames()),
                    result);
        }

        return result;
    }
}
