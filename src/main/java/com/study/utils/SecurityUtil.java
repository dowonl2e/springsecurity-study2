package com.study.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.study.exception.CustomException;
import com.study.status.ErrorCode;

public class SecurityUtil {
	
	private SecurityUtil() {}
	
	// SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
	public static Long getCurrentMemberIdx() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null || authentication.getName() == null) {
			throw new CustomException(ErrorCode.NOT_EXIST_USERINFO_TOKEN);
		}
		
		return Long.parseLong(authentication.getName());
	}
}