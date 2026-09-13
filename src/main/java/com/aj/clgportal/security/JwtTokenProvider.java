package com.aj.clgportal.security;

import java.util.Date;
import java.util.List;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.aj.clgportal.exception.UserNameNotFoundException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecret;

	@Value("${app.jwt-expiration-milliseconds}")
	private long jwtExpirationDate;

	// Generate JWT Token
	public String generateToken(Authentication auth) {

	    CustomUserDetails user =
	            (CustomUserDetails) auth.getPrincipal();

	    Date currentDate = new Date();
	    Date expirationDate =
	            new Date(currentDate.getTime() + jwtExpirationDate);

	    return Jwts.builder()
	            .setSubject(user.getUsername())

	            .claim(
	                "roles",
	                user.getAuthorities()
	                        .stream()
	                        .map(a -> a.getAuthority())
	                        .toList()
	            )

	            .claim(
	                "permissions",
	                user.getPermissions()
	            )

	            .setIssuedAt(currentDate)
	            .setExpiration(expirationDate)
	            .signWith(key())
	            .compact();
	}

	// decode secret key
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	// Get username from JWT token
	public String getUsername(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();

		String username = claims.getSubject();

		return username;
	}

	// Validate JWT Token
	public boolean validateToken(String token) {
		Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
		return true;
	}
	
	public Claims getClaims(String token) {

	    return Jwts.parserBuilder()
	            .setSigningKey(key())
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}
	
	public List<String> getPermissions(String token) {

	    return getClaims(token)
	            .get("permissions", List.class);
	}
	
	public List<String> getRoles(String token) {

	    return getClaims(token)
	            .get("roles", List.class);
	}
}
