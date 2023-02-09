package com.muhammadusman92.gatewayservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class GatewayServiceAspect {
    @Before("within(com.muhammadusman92.gatewayservice..*)")
    public void beforeLog(JoinPoint joinPoint){
        log.info("Before "+joinPoint.getSignature()+"of Gateway-Service");
    }
    @AfterReturning("within(com.muhammadusman92.gatewayservice..*)")
    public void afterLog(JoinPoint joinPoint){
        log.info("After "+joinPoint.getSignature()+"of Gateway-Service");
    }
}
