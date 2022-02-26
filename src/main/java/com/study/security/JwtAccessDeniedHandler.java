package com.study.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.study.response.ErrorResponse;
import com.study.status.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.error("JwtAccessDeniedHandler : " + ErrorCode.PROCESS_FOBIDDEN);
		setResponse(response, ErrorCode.PROCESS_FOBIDDEN);
	}
	
	private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        JSONObject responseObj = ErrorResponse.toResponseJson(errorCode);
        if(responseObj == null) {
        	response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        
        response.getWriter().print(responseObj);
    }

}
