package com.study.member.dto;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.study.member.entity.Member;
import com.study.member.values.Authority;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestDto {
	
	private String email;
	private String password;
	private String celno;
	
	public Member toEntity() {
		return Member.builder()
				.email(email)
				.password(password)
				.celno(celno)
				.authority(Authority.ROLE_USER)
				.build();
	}
	
	public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
	
}
