package com.study.status;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	//공통 에러 코드
	UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500000", "처리 중 오류가 발생하였습니다."),
	DATA_PROCESS_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "500000", "데이터 처리에 문제가 발생하였습니다."),
	PROCESS_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401000", "권한이 없습니다."),
	PROCESS_FOBIDDEN(HttpStatus.FORBIDDEN, "403000", "실행이 취소되었습니다."),

	//회원 관련
	/* 패턴
	 * HttpStatus.code + Method Code + Valid Code + 구분
	 * [Method Code]
	 * 0:GET / 1:POST / 2:PATCH / 3:DELETE
	 * 
	 * [Valid Code]
	 * 0:VALID / 1:INVALID / 2:MATCH / 3:MISMATCH / 4:EXPIRED / 5:UNSUPPORTED / 6:EXIST / 7:NOT EXIST / 8:WRONG
	 * 
	 * 
	 * 구분
	 * 1. 0 : 회원 정보
	 * 2. 1 : 이메일
	 * 3. 2 : 비밀번호
	 * 4. 8 : 토큰
	 * 5. 9 : 리프레시 토큰
	 */
	//회원 관련 에러코드
	USER_EXIST_JOIN_FAIL(HttpStatus.BAD_REQUEST, "404111", "이미 가입되어 있는 유저입니다."),
	USER_JOIN_FAIL(HttpStatus.BAD_REQUEST, "404110", "회원가입에 실패했습니다."),
	USER_INVALID(HttpStatus.BAD_REQUEST, "404010", "일치하는 회원 정보가 없습니다."),
	USER_MISMATCH_EMAIL(HttpStatus.BAD_REQUEST, "404031", "일치하는 이메일 정보가 없습니다."),
	USER_MISMATCH_PWD(HttpStatus.BAD_REQUEST, "404032", "비밀번호가 틀렸습니다."),
	
	//토큰
	INVALID_AUTH_TOKEN(HttpStatus.BAD_REQUEST, "404018", "권한 정보가 없는 토큰입니다."),
	UNSUPPORTED_AUTH_TOKEN(HttpStatus.BAD_REQUEST, "404058", "지원되지 않는 토큰입니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "404019", "유효하지 않는 리프레시 토큰입니다."),
	MISMATCH_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "404039", "리프레시 토큰의 유저 정보가 일치하지 않습니다."),
	EXPIRED_AUTH_TOKEN(HttpStatus.BAD_REQUEST , "404048", "만료된 토큰입니다."),
	NOT_EXIST_AUTH_TOKEN(HttpStatus.BAD_REQUEST, "404078", "토큰 정보가 없습니다."),
	WRONG_AUTH_TOKEN(HttpStatus.BAD_REQUEST, "404088", "토큰 정보가 없습니다.");
	
	private HttpStatus httpStatus;
	private String code;
	private String message;
}
