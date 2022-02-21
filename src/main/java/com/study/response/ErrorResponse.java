package com.study.response;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

import com.study.status.ErrorCode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse{
	
	private final LocalDateTime timestamp = LocalDateTime.now();
	private final int status;
	private final String code;
	private final String message;
	
	public ErrorResponse(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
	
	public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode){
		return ResponseEntity
				.status(errorCode.getHttpStatus())
				.body(ErrorResponse.builder()
						.status(errorCode.getHttpStatus().value())
						.code(errorCode.getCode())
						.message(errorCode.getMessage())
						.build());
	}
}
