package com.study.exception;

import com.study.status.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
	
	private final ErrorCode errorCode;

}
