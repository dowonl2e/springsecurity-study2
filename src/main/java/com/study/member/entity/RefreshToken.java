package com.study.member.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {

	@Id
	@Column(name = "key_id")
	private String keyId;
	
	@Column(name = "key_value")
	private String keyValue;
	
	private LocalDateTime expiredDate;
	private LocalDateTime createdDate = LocalDateTime.now();
	private LocalDateTime modifiedDate;
	
	@Builder
	public RefreshToken(String keyId, String keyValue, LocalDateTime expiredDate) {
		this.keyId = keyId;
		this.keyValue = keyValue;
		this.expiredDate = expiredDate;
	}
	
	public RefreshToken update(String keyValue, LocalDateTime expiredDate) {
		this.keyValue = keyValue;
		this.expiredDate = expiredDate;
		this.modifiedDate = LocalDateTime.now();
		return this;
	}
	
	
}
