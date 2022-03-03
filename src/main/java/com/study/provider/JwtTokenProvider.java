package com.study.provider;

import java.security.Key;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.study.exception.CustomException;
import com.study.member.dto.TokenDto;
import com.study.status.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private static final String AUTHORIZATION = "Authorization";
	private static final String AUTH_TYPE = "Bearer";
	
	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
	
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; //7일
	
	private final Key key;
	
	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		byte[] keyBytes = Base64.getDecoder().decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}
	
	/**
	 * Access Token, Refresh Token 생성
	 * 사용자 정보(authentication.getName())를 가져와 토큰을 생성한다.
	 * @param authentication
	 * @return
	 */
	public TokenDto generateTokenDto(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		Date now = new Date();
		//AccessToken 생성
		Date accessTokenExpiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
		String accessToken = Jwts.builder()
				.setSubject(authentication.getName()) 		// payload "sub": "name"
				.setIssuedAt(now)
				.claim(AUTHORIZATION, authorities)		// payload "Authrization": "ROLE_USER"
				.setExpiration(accessTokenExpiresIn)		// payload "exp": 1516239022 (예시)
				.signWith(key, SignatureAlgorithm.HS512)	// header "alg": "HS512"
				.compact();
		
		
		Date refreshTokenExpiredIn = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);
		String refreshToken = Jwts.builder()
				.setExpiration(refreshTokenExpiredIn)
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
		
		return TokenDto.builder()
				.grantType(AUTH_TYPE)
				.accessToken(accessToken)
				.accessTokenExpioresIn(accessTokenExpiresIn.getTime())
				.refreshToken(refreshToken)
				.refreshExpiredDate(refreshTokenExpiredIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
				.build();
	}
	
	/**
	 * 토큰을 복호화하여 토큰에 들어있는 정보를 가져온다.
	 * AccessToken에만 유저 정보를 담고 있다.
	 * SecurityContext가 Authentication를 지니고 있기에 UserDetails는 SecurityContext를 사용하기 위함이다.
	 * @param accessToken
	 * @return
	 */
	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);
		
		if(claims.get(AUTHORIZATION) == null) {
			throw new CustomException(ErrorCode.NOT_EXIST_AUTH_TOKEN);
		}
		
		Collection<? extends GrantedAuthority> authorities = 
				Arrays.stream(claims.get(AUTHORIZATION).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
		UserDetails principal = new User(claims.getSubject(), "", authorities);
		
		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
		
	}
	
	/**
	 * 토큰을 검증한다.
	 * Jwts 모듈이 토큰 검증에 따라 Exception을 던진다.
	 * @param token
	 * @return
	 */
	public boolean isValidToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
		return false;
	}
    
	/**
	 * 토큰을 복호화 한다.
	 * @param accessToken
	 * @return
	 */
	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

    /**
     * 복호화된 토큰에서 name 추출한다.
     * @param accessToken
     * @return
     */
    public String getSubject(String accessToken) {
		return parseClaims(accessToken).getSubject();
    }

}
