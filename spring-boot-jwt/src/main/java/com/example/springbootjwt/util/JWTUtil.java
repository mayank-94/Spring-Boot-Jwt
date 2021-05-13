package com.example.springbootjwt.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	private static String SECRET_KEY = "secret";
	
	public String getUsername(final String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date getExpiration(final String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <R> R extractClaim(final String token, final Function<Claims, R> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	//Claims are the properties that we set in the Payload
	private Claims getAllClaims(final String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	private Boolean isTokenExpired(final String token) {
		return getExpiration(token).before(new Date());
	}
	
	public String generateToken(final UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(final Map<String, Object> claims, final String username) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}
	
	public Boolean validateToken(final String token, final UserDetails userdetails) {
		final String username = getUsername(token);
		return username.equals(userdetails.getUsername()) 
				&& !isTokenExpired(token);
	}
}
