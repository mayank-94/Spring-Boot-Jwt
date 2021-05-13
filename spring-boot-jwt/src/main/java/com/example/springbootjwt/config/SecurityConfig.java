package com.example.springbootjwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.springbootjwt.filter.JwtAuthEntryPoint;
import com.example.springbootjwt.filter.JwtRequestFilter;
import com.example.springbootjwt.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final MyUserDetailsService myUserDetailsService;
	private final JwtRequestFilter jwtRequestFilter;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtAuthEntryPoint jwtAuthEntryPoint;
	
	@Autowired
	public SecurityConfig(final MyUserDetailsService myUserDetailsService,
			final JwtRequestFilter jwtRequestFilter, final BCryptPasswordEncoder passwordEncoder,
			final JwtAuthEntryPoint jwtAuthEntryPoint) {
		super();
		this.myUserDetailsService = myUserDetailsService;
		this.jwtRequestFilter = jwtRequestFilter;
		this.passwordEncoder = passwordEncoder;
		this.jwtAuthEntryPoint = jwtAuthEntryPoint;
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf()
				.disable().cors().and()
			.authorizeRequests()
			.antMatchers("/auth").permitAll()
			.anyRequest().authenticated();
		
		http.exceptionHandling()
			.authenticationEntryPoint(jwtAuthEntryPoint);
		
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //telling spring not to manage sessions
			
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);
	}
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
}
