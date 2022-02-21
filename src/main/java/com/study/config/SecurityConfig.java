package com.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.study.filter.JwtAuthenticationFilter;
import com.study.member.values.Authority;
import com.study.provider.JwtTokenProvider;
import com.study.security.JwtAccessDeniedHandler;
import com.study.security.JwtAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtAccessDeniedHandler accessDeniedHandler;
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
		web.ignoring().antMatchers("/static/**", "/favicon.ico");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {      
		http
	        .csrf().disable() // Rest API : CSRF보안이 필요 없으므로 미사용 처리(Rest 방식의 경우 미사용 처리 필요함)
        	.httpBasic().disable() // Rest API : 기본설정 사용안함(기본설정은 비인증시 로그인화면으로 리다이렉트 됨)
        	// Spring Security는 기본적으로 세션을 사용하므로 세션은 미사용 설정
	        .sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT 토큰으로 인증 하므로 세션은 필요 없어 생성하지 않음

	        //exception handling 추가
        	.and()
        	.exceptionHandling()
        	.accessDeniedHandler(accessDeniedHandler)
        	.authenticationEntryPoint(authenticationEntryPoint)
        	
			.and()
			.authorizeRequests() //다음 Request에 대한 사용 권한 체크
			.antMatchers("/api/auth/**").permitAll() // 가입 및 인증 API는 누구나 접근가능
			.anyRequest().authenticated() //외 나머지 API는 인증 필요
            // .anyRequest().hasRole(Authority.ROLE_USER.name()) // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
            // .anyRequest().hasRole(Authority.ROLE_ADMIN.name()) // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
            
	        .and()
	        .apply(new JwtSecurityConfig(jwtTokenProvider));

	}
	
}
