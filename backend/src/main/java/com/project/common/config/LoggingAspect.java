package com.project.common.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {
    }

    @Around("controllerPointcut() || servicePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        
        if (log.isDebugEnabled()) {
            log.debug("Вход в метод: {}.{}() с аргументами = {}", className, methodName, Arrays.toString(joinPoint.getArgs()));
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Object result = joinPoint.proceed();
            
            stopWatch.stop();
            if (log.isDebugEnabled()) {
                log.debug("Выход из метода: {}.{}() с результатом = {}. Время выполнения: {} мс", 
                        className, methodName, result, stopWatch.getTotalTimeMillis());
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Неверные аргументы в методе: {}.{}()", className, methodName, e);
            throw e;
        } catch (Exception e) {
            log.error("Exception в методе: {}.{}() - {}", className, methodName, e.getMessage(), e);
            throw e;
        }
    }
}