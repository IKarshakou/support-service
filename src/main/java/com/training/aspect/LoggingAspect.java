package com.training.aspect;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {

    private static final String LOG_MESSAGE = "Class [{}]: User [{}] called method [{}] with args [{}] and result is: ";
    private static final String METHOD_RETURN_VOID = "void";
    private static final String METHOD_RETURN_VALUE = "{}";
    private static final String PROCESS_INSTEAD_OF_USERNAME = "System process";
    private static final String TAG_CLASS = "Class";
    private static final String TAG_METHOD = "Method";
    private static final String TAG_ARGUMENTS = "Arguments";
    private static final String TAG_RESULT = "Result";
    private static final String TAG_ERROR = "Error";
    private static final String TAG_STACK_TRACE = "StackTrace";
    private static final String MDC_CLASS_NAME_KEY = "className";

    private final Tracer tracer;

    @Around("Pointcuts.allProjectMethods() && Pointcuts.noOneFromFilterPackage() && Pointcuts.noOneFromMapperPackage()"
            + " && Pointcuts.notSecurityConfig() && !within(com.training.scheduled.*)"
            + " && !target(com.training.repository.EmployeeTicketMatViewRepository)")
    public Object aroundServiceMethodsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        Authentication authentication;
        String username = PROCESS_INSTEAD_OF_USERNAME;
        String className = null;
        String methodName = null;
        String methodArguments = null;

        Span newSpan = tracer.nextSpan(tracer.currentSpan()).name(joinPoint.getSignature().toShortString());
        try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan.start())) {
            authentication = SecurityContextHolder.getContext()
                    .getAuthentication();

            if (authentication != null) {
                username = authentication.getName();
            }

            result = joinPoint.proceed();

            MDC.put(MDC_CLASS_NAME_KEY, joinPoint.getSignature().getDeclaringType().getName());

            className = joinPoint.getSignature().getDeclaringType().getName();
            methodName = joinPoint.getSignature().getName();
            methodArguments = Arrays.toString(joinPoint.getArgs());

            newSpan.tag(TAG_CLASS, className);
            newSpan.tag(TAG_METHOD, methodName);
            newSpan.tag(TAG_ARGUMENTS, methodArguments);

            if (result == null) {
                log.info(LOG_MESSAGE + METHOD_RETURN_VOID,
                        className,
                        username,
                        methodName,
                        methodArguments);
            } else {
                log.info(LOG_MESSAGE + METHOD_RETURN_VALUE,
                        className,
                        username,
                        methodName,
                        methodArguments,
                        result);
                newSpan.tag(TAG_RESULT, result.toString());
            }
        } catch (Throwable throwable) {
            log.error(LOG_MESSAGE + throwable.getMessage(),
                    className,
                    username,
                    methodName,
                    methodArguments);

            newSpan.tag(TAG_ERROR, throwable.getLocalizedMessage());
            newSpan.tag(TAG_STACK_TRACE, Arrays.toString(throwable.getStackTrace()));
            newSpan.error(throwable);

            throw throwable;
        } finally {
            newSpan.end();
            MDC.remove(MDC_CLASS_NAME_KEY);
        }

        return result;
    }
}
