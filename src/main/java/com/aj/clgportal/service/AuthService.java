package com.aj.clgportal.service;

import com.aj.clgportal.dto.LoginDto;

public interface AuthService {
	//String register(RegisterDto registerDto);
    String login(LoginDto loginDto);
}
