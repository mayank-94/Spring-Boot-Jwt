package com.example.springbootjwt.filter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException authException) throws IOException, ServletException {
		log.info("----Entering into JwtAuthEntryPoint----");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		String message;
		if(authException.getCause() != null) {
			message = authException.getCause().toString() + " " + 
					authException.getMessage().toString();
		}else {
			message = authException.getMessage();
		}
		
		byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));
		response.getOutputStream().write(body);
	}
}
