package com.study.member.dto;

import com.study.member.entity.Member;
import com.study.member.values.Authority;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
	
	private Long idx;
	private String email;
	private String password;
	private String celno;
	private Authority authority;
	
	public MemberResponseDto(Member entity) {
		this.idx = entity.getIdx();
		this.email = entity.getEmail();
		this.celno = entity.getCelno();
		this.authority = entity.getAuthority();
	}
}
