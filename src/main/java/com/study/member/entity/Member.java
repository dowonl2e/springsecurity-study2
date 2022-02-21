package com.study.member.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.study.member.values.Authority;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {

	@Id
	@Column(name = "member_idx")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;
	
	private String email;
	
	private String password;
	
	private String celno;
	
	@Enumerated(EnumType.STRING)
	private Authority authority;
	
	private char resignYn = 'N';
	
	private LocalDateTime createdDate = LocalDateTime.now();
	
	private LocalDateTime modifiedDate;
	
	private LocalDateTime resignDate; 
	
	@Builder
	public Member(String email, String password, String celno, Authority authority) {
		this.email = email;
		this.password = password;
		this.celno = celno;
		this.authority = authority;
	}
	
	
}
