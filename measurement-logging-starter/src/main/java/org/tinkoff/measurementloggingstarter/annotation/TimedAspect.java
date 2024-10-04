package org.tinkoff.measurementloggingstarter.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimedAspect {

    @Around("execution(* *(..)) && (@within(org.tinkoff.measurementloggingstarter.annotation.Timed) || @annotation(org.tinkoff.measurementloggingstarter.annotation.Timed))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long initTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - initTime;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = signature.getDeclaringTypeName();

        log.info("Class: {}, Method: {}", className, methodName);
        log.info("Method executed in: {} ms", executionTime);
        return proceed;
    }
}