package com.study.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.exception.CustomException;
import com.study.member.dto.MemberResponseDto;
import com.study.member.entity.MemberRepository;
import com.study.status.ErrorCode;
import com.study.utils.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository memberRepository;
	
	/**
	 * 회원 인덱스(PK)로 조회
	 * @param idx
	 * @return
	 */
	@Transactional(readOnly = true)
	public MemberResponseDto getMember(long idx) {
		return memberRepository.findById(idx)
				.map(MemberResponseDto::new).orElseThrow(() -> new CustomException(ErrorCode.USER_INVALID));
	}
	
	/**
	 * 이메일로 조회
	 * @param email
	 * @return
	 */
	@Transactional(readOnly = true)
	public MemberResponseDto getMember(String email) {
		return memberRepository.findByEmail(email)
				.map(MemberResponseDto::new).orElseThrow(() -> new CustomException(ErrorCode.USER_MISMATCH_EMAIL));
	}
	
	/**
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public MemberResponseDto getMember() {
		return memberRepository.findById(SecurityUtil.getCurrentMemberIdx())
				.map(MemberResponseDto::new).orElseThrow(() -> new CustomException(ErrorCode.USER_INVALID));
	}
	
	
}
