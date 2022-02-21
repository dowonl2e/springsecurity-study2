package com.study.status;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
	
	//회원 관련 에러
	//패턴
	//HttpStatus.code + Method(0:GET / 1:POST/ 2:PATCH / 3:DELETE) + 사용자별 패턴
	
	//회원 관련 성공코드
	USER_JOIN_SUCCESS(HttpStatus.OK, "200100", "회원가입이 완료되었습니다."),
	USER_FIND_SUCCESS(HttpStatus.OK, "200000", "회원 정보 정상 조회되었습니다.");
	

	private HttpStatus httpStatus;
	private String code;
	private String message;
}
