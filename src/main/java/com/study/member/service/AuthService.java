package com.study.member.service;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.exception.CustomException;
import com.study.member.dto.MemberRequestDto;
import com.study.member.dto.MemberResponseDto;
import com.study.member.dto.TokenDto;
import com.study.member.dto.TokenRequestDto;
import com.study.member.entity.MemberRepository;
import com.study.member.entity.RefreshToken;
import com.study.member.entity.RefreshTokenRepository;
import com.study.provider.JwtTokenProvider;
import com.study.status.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		if(authentication.isAuthenticated() == false)
			throw new CustomException(ErrorCode.USER_MISMATCH_PWD);    		//인증 정보를 기반으로 Token 생성
		
		//인증정보 SecurityContextHolder에 추가
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//인증 정보를 기반으로 Token 생성
		TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);
		
        Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findByKeyId(authentication.getName());
        //refreshToken 정보 여부 및 만료여부 체크
        if(refreshTokenEntity.isEmpty()) {
    		//Token 저장
    		RefreshToken refreshToken = RefreshToken.builder()
    				.keyId(authentication.getName())
    				.keyValue(tokenDto.getRefreshToken())
    				.expiredDate(tokenDto.getRefreshExpiredDate())
    				.build();
    		
    		refreshTokenRepository.save(refreshToken);	
        }
        else {
        	//Refresh Token 만료 체크
            if (jwtTokenProvider.isValidToken(refreshTokenEntity.get().getKeyValue()) == false)
        		refreshTokenEntity.get().update(tokenDto.getRefreshToken(), tokenDto.getRefreshExpiredDate());
            else
            	tokenDto
            		.setRefreshToken(refreshTokenEntity.get().getKeyValue())
            		.setRefreshExpiredDate(refreshTokenEntity.get().getExpiredDate());
        }
		
		
		return tokenDto;
	}
	
	@Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        if (jwtTokenProvider.isValidToken(tokenRequestDto.getRefreshToken()) == false) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // Access Token 에서 Member ID 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKeyId(authentication.getName())
        		.orElseThrow(() -> new CustomException(ErrorCode.USER_LOGOUT));

        // Refresh Token 검사
        if (!refreshToken.getKeyValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new CustomException(ErrorCode.MISMATCH_REFRESH_TOKEN);
        }

        // 새로운 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // 6. RefreshToken 정보 업데이트
        refreshToken.update(tokenDto.getRefreshToken(), tokenDto.getRefreshExpiredDate());

        return tokenDto;
    }

}
