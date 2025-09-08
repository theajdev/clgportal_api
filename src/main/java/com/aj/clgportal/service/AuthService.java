package com.aj.clgportal.service;

import com.aj.clgportal.dto.LoginDto;
import com.aj.clgportal.dto.UserResponseDto;

public interface AuthService {
	//String register(RegisterDto registerDto);
    String login(LoginDto loginDto);
    String getAuthority(LoginDto loginDto);
    UserResponseDto getUserDetailsByRole(LoginDto loginDto);
}
