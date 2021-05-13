package com.example.springbootjwt.response;

import lombok.Getter;

@Getter
public class AuthenticateResponse {
	private final String jwt;

	public AuthenticateResponse(final String jwt) {
		super();
		this.jwt = jwt;
	}
}
