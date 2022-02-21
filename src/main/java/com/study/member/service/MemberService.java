package com.study.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.member.dto.MemberResponseDto;
import com.study.member.entity.MemberRepository;
import com.study.utils.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository memberRepository;
	
	@Transactional(readOnly = true)
	public MemberResponseDto getMember(String email) {
		return memberRepository.findByEmail(email)
				.map(MemberResponseDto::new).orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));
	}
	
	@Transactional(readOnly = true)
	public MemberResponseDto getMember() {
		return memberRepository.findById(SecurityUtil.getCurrentMemberIdx())
				.map(MemberResponseDto::new).orElseThrow(() -> new RuntimeException("로그인 정보가 없습니다."));
	}
	
	
}
