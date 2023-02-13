package com.training.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.training.service..*(..))")
    public void allServiceMethods() {}

    @Pointcut("execution(* com.training..*(..))")
    public void allProjectMethods() {}

    @Pointcut("within(@com.training.annotation.LoggedApi *)")
    public void loggedApiClass() {}

    @Pointcut("@annotation(com.training.annotation.LoggedApi)")
    public void loggedApiMethod() {}

    @Pointcut("execution(public * *(..))")
    public void publicExecution() {}

    @Pointcut("execution(* *(..))")
    public void anyExecution() {}

    @Pointcut("!within(com.training.filter.*)")
    public void noOneFromFilterPackage() {}

    @Pointcut("!within(com.training.mapper.*)")
    public void noOneFromMapperPackage() {}

    @Pointcut("!target(com.training.config.SecurityConfig)")
    public void notSecurityConfig() {}
}
