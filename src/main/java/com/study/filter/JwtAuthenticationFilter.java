package com.study.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	private static final String AUTHORIZATION_HEADER = "X-AUTH-TOKEN";
	private static final String BEARER_PREFIX = "Bearer ";
	
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = resolveToken(request);
		String requestURI = request.getRequestURI();
		
		if (StringUtils.hasText(token) && jwtTokenProvider.isValidToken(token)) {
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("Security Context에 "+authentication.getName()+" 인증 정보를 저장했습니다, URI : " + requestURI);
		}
		else {
			log.debug("유효한 JWT 토큰이 없습니다, URI : " + requestURI);
		}
		
		filterChain.doFilter(request, response);
	}
	
	/*
     * HTTP Request 헤더에서 토큰만 추출하기 위한 메서드
     */
	private String resolveToken(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION_HEADER);
		if(StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
			return token.substring(0,7);
		}
		return null;
	}
}
