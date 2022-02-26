package com.study.member.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
	
	private String grantType;
	private String accessToken;
	private String refreshToken;
	private Long accessTokenExpioresIn;
	private LocalDateTime refreshExpiredDate;
	
	public TokenDto setGrantType(String grantType) {
		this.grantType = grantType;
		return this;
	}
	public TokenDto setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}
	public TokenDto setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}
	public TokenDto setAccessTokenExpioresIn(Long accessTokenExpioresIn) {
		this.accessTokenExpioresIn = accessTokenExpioresIn;
		return this;
	}
	public TokenDto setRefreshExpiredDate(LocalDateTime refreshExpiredDate) {
		this.refreshExpiredDate = refreshExpiredDate;
		return this;
	}
	
	
}
