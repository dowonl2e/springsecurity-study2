package com.study.member.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.exception.CustomException;
import com.study.member.dto.MemberResponseDto;
import com.study.member.service.MemberService;
import com.study.response.BasicResponse;
import com.study.status.ResponseCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

	private final MemberService memberService;
	
	@GetMapping("/member")
	public ResponseEntity<BasicResponse<MemberResponseDto>> getMember(){
		Optional<MemberResponseDto> responseDto = Optional.ofNullable(memberService.getMember());
		return ResponseEntity.ok().body(
					new BasicResponse<MemberResponseDto>(ResponseCode.USER_FIND_SUCCESS, responseDto.get())
				);
	}
	
//	@GetMapping("/members/{email}")
//	public ResponseEntity<BasicResponse<MemberResponseDto>> getMember(@PathVariable final String email){
//		Optional<MemberResponseDto> responseDto = Optional.ofNullable(memberService.getMember(email));
//		return ResponseEntity.ok().body(
//				new BasicResponse<MemberResponseDto>(ResponseCode.USER_FIND_SUCCESS, responseDto.get())
//			);
//	}
	
	@GetMapping("/members/{idx}")
	public ResponseEntity<BasicResponse<MemberResponseDto>> getMember(@PathVariable final long idx){
		Optional<MemberResponseDto> responseDto = Optional.ofNullable(memberService.getMember(idx));
		return ResponseEntity.ok().body(
				new BasicResponse<MemberResponseDto>(ResponseCode.USER_FIND_SUCCESS, responseDto.get())
			);
	}
}
