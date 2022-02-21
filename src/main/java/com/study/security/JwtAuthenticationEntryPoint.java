package com.study.security;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.study.status.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.error("JwtAuthenticationEntryPoint : " + ErrorCode.PROCESS_UNAUTHORIZED);
		setResponse(response, ErrorCode.PROCESS_UNAUTHORIZED);
	}
	
	//한글 출력을 위해 getWriter() 사용
    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JSONObject responseObj = new JSONObject();
        try {
        	responseObj.put("status", errorCode.getHttpStatus().value());
	        responseObj.put("code", errorCode.getCode());
	        responseObj.put("message", errorCode.getMessage());
        } catch(JSONException e) {
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        response.getWriter().print(responseObj);
    }

}
