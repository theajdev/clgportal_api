package com.aj.clgportal.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	private UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		
		
		return null;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		//String path = request.getRequestURI();

		if (request.getRequestURI().startsWith("/ws")) {
		    filterChain.doFilter(request, response);
		    return;
		}
		
		
		// Get JWT Token from HTTP Request

		String token = getTokenFromRequest(request);

		// Validate token
		if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			// get username from token
			String username =
			        jwtTokenProvider.getUsername(token);

			List<String> roles =
			        jwtTokenProvider.getRoles(token);

			List<String> permissions =
			        jwtTokenProvider.getPermissions(token);
			
			
			

			List<SimpleGrantedAuthority> authorities =
			        roles.stream()
			             .map(SimpleGrantedAuthority::new)
			             .toList();
			
			UsernamePasswordAuthenticationToken authenticationToken =
			        new UsernamePasswordAuthenticationToken(
			                username,
			                null,
			                authorities);

			authenticationToken.setDetails(permissions);
			
			SecurityContextHolder.getContext()
	        .setAuthentication(authenticationToken);
		}

		filterChain.doFilter(request, response);

	}

}
