package com.aj.clgportal.service;

import org.springframework.http.ResponseEntity;

import com.aj.clgportal.dto.ChangePasswordRequest;

public interface UserPasswordRequestService {
	public ResponseEntity<?> changePassword(ChangePasswordRequest request);
}
