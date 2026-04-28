package com.aj.clgportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aj.clgportal.dto.ChangePasswordRequest;
import com.aj.clgportal.service.UserPasswordRequestService;

@RestController
@RequestMapping("/api/user")
public class UserPasswordController {
	@Autowired
	private UserPasswordRequestService userService;

	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest userDetails) {
		return userService.changePassword(userDetails);
	}
}
