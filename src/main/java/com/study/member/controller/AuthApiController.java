package com.study.member.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.member.dto.MemberRequestDto;
import com.study.member.dto.MemberResponseDto;
import com.study.member.dto.TokenDto;
import com.study.member.dto.TokenRequestDto;
import com.study.member.service.AuthService;
import com.study.response.BasicResponse;
import com.study.status.ResponseCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {
	
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<BasicResponse<MemberResponseDto>> singUp(@RequestBody final MemberRequestDto memberRequestDto){
		Optional<MemberResponseDto> responseDto = Optional.of(authService.signup(memberRequestDto));
		return ResponseEntity.ok().body(
				new BasicResponse<MemberResponseDto>(ResponseCode.USER_JOIN_SUCCESS, responseDto.get())
			);
	}
	
	
	@PostMapping("/signin")
	public ResponseEntity<BasicResponse<TokenDto>> singIn(@RequestBody final MemberRequestDto memberRequestDto){
		Optional<TokenDto> responseDto = Optional.of(authService.signin(memberRequestDto));
		return ResponseEntity.ok().body(
				new BasicResponse<TokenDto>(ResponseCode.USER_FIND_SUCCESS, responseDto.get())
			);
	}
	
	@PostMapping("/reissue")
    public ResponseEntity<BasicResponse<TokenDto>> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
		Optional<TokenDto> responseDto = Optional.of(authService.reissue(tokenRequestDto));

        return ResponseEntity.ok().body(
        		new BasicResponse<TokenDto>(ResponseCode.TOKEN_CREATE_SUCCESS, responseDto.get())
    		);
    }
	
}
