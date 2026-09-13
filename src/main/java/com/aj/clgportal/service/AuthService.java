package com.aj.clgportal.service;

import com.aj.clgportal.dto.LoginDto;
import com.aj.clgportal.dto.UserResponseDto;

public interface AuthService {
    UserResponseDto getUserDetailsByRole(LoginDto loginDto);
}
