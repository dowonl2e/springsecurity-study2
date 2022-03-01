package com.study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.study.interceptor.AuthInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	private final AuthInterceptor authInterceptor;

	public WebMvcConfig(AuthInterceptor authInterceptor) {
		this.authInterceptor = authInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.debug(">>> WebMvcConfig addInterceptor");
		registry.addInterceptor(authInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/api/auth/**")
			.excludePathPatterns("/static");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	
	
}
