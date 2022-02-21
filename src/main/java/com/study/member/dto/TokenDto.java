package com.study.member.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
	
	private String grantType;
	private String accessToken;
	private String refreshToken;
	private Long accessTokenExpioresIn;
	private LocalDateTime refreshExpiredDate;
}
