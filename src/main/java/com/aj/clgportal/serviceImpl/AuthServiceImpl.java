package com.aj.clgportal.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.LoginDto;
import com.aj.clgportal.exception.UserNameNotFoundException;
import com.aj.clgportal.security.JwtTokenProvider;
import com.aj.clgportal.service.AuthService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private AuthenticationManager authenticationManager;
	
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public String login(LoginDto loginDto)throws UserNameNotFoundException{
		try {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtTokenProvider.generateToken(authenticate);
        return token;
		}catch(BadCredentialsException ex) {
			throw new UserNameNotFoundException("Invalid username or password.");
		}
	}

}
