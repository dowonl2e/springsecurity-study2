package com.study.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.study.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION = "Authorization";
	private static final String AUTH_TYPE = "Bearer";
	
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = resolveToken(request);
		if (StringUtils.hasText(token) && jwtTokenProvider.isValidToken(token)) {
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.info("Security Context에 '"+authentication.getName()+"' 인증 정보를 저장했습니다.");
		}
		
		filterChain.doFilter(request, response);
	}
	
	/*
     * HTTP Request 헤더에서 토큰만 추출하기 위한 메서드
     */
	private String resolveToken(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION);
		if(StringUtils.hasText(token) && token.startsWith(AUTH_TYPE)) {
			return token.substring(7);
		}
		return null;
	}
}
