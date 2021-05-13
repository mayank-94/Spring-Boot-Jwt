package com.example.springbootjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootjwt.request.AuthenticateRequest;
import com.example.springbootjwt.response.AuthenticateResponse;
import com.example.springbootjwt.util.JWTUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class Controller {
	
	private final AuthenticationManager authManager;
	private final UserDetailsService userDetailsService;
	private final JWTUtil jwtUtil;
	
	@Autowired
	public Controller(final AuthenticationManager authManager,
			final UserDetailsService userDetailsService, final JWTUtil jwtUtil) {
		super();
		this.userDetailsService = userDetailsService;
		this.authManager = authManager;
		this.jwtUtil = jwtUtil;
	}

	@GetMapping(value = "/admin")
	public String helloAdmin() {
		return "Hello Admin";
	}
		
	@PostMapping(value = "/auth")
	public ResponseEntity<?> createAuthToken(@RequestBody AuthenticateRequest request) throws Exception{
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
				);
			log.info("Successfully Authenticated!!");
			final UserDetails userDetails = userDetailsService
					.loadUserByUsername(request.getUsername());
			final String jwtToken = jwtUtil.generateToken(userDetails);
			
			log.info("JWT created successfully!!");
			return ResponseEntity.ok(new AuthenticateResponse(jwtToken));
		} catch (BadCredentialsException ex) {
			log.error("Authentication failed. Incorrect credentials!!");
			throw new Exception(ex.getMessage());
		}
	}
}
