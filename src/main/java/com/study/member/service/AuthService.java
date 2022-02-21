package com.study.member.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.exception.CustomException;
import com.study.member.dto.MemberRequestDto;
import com.study.member.dto.MemberResponseDto;
import com.study.member.dto.TokenDto;
import com.study.member.entity.MemberRepository;
import com.study.member.entity.RefreshToken;
import com.study.member.entity.RefreshTokenRepository;
import com.study.provider.JwtTokenProvider;
import com.study.status.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	
	@Transactional
	public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
		if(memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.USER_EXIST_JOIN_FAIL);
		}
		
        memberRequestDto.setPassword(passwordEncoder.encode(memberRequestDto.getPassword()));
        return new MemberResponseDto(memberRepository.save(memberRequestDto.toEntity()));
	}
	
	@Transactional
	public TokenDto signin(MemberRequestDto memberRequestDto) {
		//로그인 ID/PW 기반으로 AuthenticationToken 객체 생성
		UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

		//실제 검증 
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		System.out.println("인증 체크 : " + authentication.getName() + ", " + authentication.isAuthenticated());
		if(authentication.isAuthenticated() == false)
			throw new CustomException(ErrorCode.USER_MISMATCH_PWD);
		
		
		//인증 정보를 기반으로 Token 생성
		TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);
		
		//Token 저장
		RefreshToken refreshToken = RefreshToken.builder()
				.keyId(authentication.getName())
				.keyValue(tokenDto.getRefreshToken())
				.expiredDate(tokenDto.getRefreshExpiredDate())
				.build();
		
		refreshTokenRepository.save(refreshToken);
		
		return tokenDto;
	}
	
}
