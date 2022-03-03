package com.study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.study.interceptor.AuthInterceptor;
import com.study.interceptor.LoggerInterceptor;

import lombok.AllArgsConstructor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	private final AuthInterceptor authInterceptor;
	private final LoggerInterceptor loggerInterceptor;
	
	public WebMvcConfig(AuthInterceptor authInterceptor, LoggerInterceptor loggerInterceptor){
		this.authInterceptor = authInterceptor;
		this.loggerInterceptor = loggerInterceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggerInterceptor)
			.excludePathPatterns("/css/**", "/fonts/**", "/plugin/**", "/scripts/**", "/favicon**");
		registry.addInterceptor(authInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/api/auth/**", "/static");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	
	
}
