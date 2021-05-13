package com.example.springbootjwt.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public MyUserDetailsService(final BCryptPasswordEncoder passwordEncoder) {
		super();
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		if(!username.equals("admin")) {
			throw new UsernameNotFoundException("Username does not exist!!");
		}
				
		return new User(username, passwordEncoder.encode("1234"), new ArrayList<>());
	}
	
}
