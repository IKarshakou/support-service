package com.training.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.training.service..*(..))")
    public void allServiceMethods() {}

    @Pointcut("within(@com.training.annotation.LoggedApi *)")
    public void loggedApiClass() {}

    @Pointcut("@annotation(com.training.annotation.LoggedApi)")
    public void loggedApiMethod() {}

    @Pointcut("execution(public * *(..))")
    public void publicExecution() {}

    @Pointcut("execution(* *(..))")
    public void anyExecution() {}
}
