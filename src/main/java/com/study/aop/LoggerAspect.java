package com.study.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class LoggerAspect {

	@Around("execution(* com.study.member..controller.*Controller.*(..))"
			+ "or execution(* com.study.member..service.*Service.*(..))")
	public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
		
		String type = "";
		String name = joinPoint.getSignature().getDeclaringTypeName();
		
		Object[] params = joinPoint.getArgs();
		
		if(name.contains("Controller"))
			type = "Controller ==> ";
		else if(name.contains("Service"))
			type = "Service ==> ";
		
		log.debug(type + name + "." + joinPoint.getSignature().getName() + "()");

		if(!ObjectUtils.isEmpty(params)) {
			for(Object param : params) {
				if(!ObjectUtils.isEmpty(param)) {
					log.debug("Parameter ==> " + param.toString());
				}
			}
		}
		return joinPoint.proceed();
	}
}
