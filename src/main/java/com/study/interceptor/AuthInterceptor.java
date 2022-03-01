package com.study.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.study.exception.CustomException;
import com.study.provider.AuthorizationExtractor;
import com.study.provider.JwtTokenProvider;
import com.study.status.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
	
	private final AuthorizationExtractor authorizationExtractor;
	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		log.debug("AuthInterceptor preHandle() : " + request.getRequestURI());
		String token = authorizationExtractor.extract(request, "Bearer");
		
		if(token == null) {
			throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
		}
		log.debug("토큰3 : " + token);
		if(jwtTokenProvider.isValidToken(token) == false) {
			throw new CustomException(ErrorCode.EXPIRED_AUTH_TOKEN);
		}
		
		String name = jwtTokenProvider.getSubject(token);
		request.setAttribute("name", name);
		return true;
	}
	
}
