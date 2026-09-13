package com.aj.clgportal.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aj.clgportal.dto.JwtAuthResponse;
import com.aj.clgportal.dto.LoginDto;
import com.aj.clgportal.dto.UserResponseDto;
import com.aj.clgportal.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@SuppressWarnings("unused")
	private final DepartmentController departmentController;

	@SuppressWarnings("unused")
	private final ModelMapper ModelMapper;

	@Autowired
	private AuthService authServ;

	/*
	 * @Autowired private CaptchaService captchaService;
	 */

	AuthController(ModelMapper ModelMapper, DepartmentController departmentController) {
		this.ModelMapper = ModelMapper;
		this.departmentController = departmentController;
	}

	// build Login REST API
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request) {

		/*
		 * boolean captchaVerified =
		 * captchaService.verifyCaptcha(loginDto.getRecaptchaToken()); if
		 * (!captchaVerified) { return ResponseEntity .status(HttpStatus.BAD_REQUEST)
		 * .body("Captcha verification failed. Please try again."); }
		 */

		System.out.println("UserName: "+loginDto.getUsernameOrEmail());
		System.out.println("Password: "+loginDto.getPassword());
		System.out.println("Auth: "+loginDto.getAuthority());
		UserResponseDto user = authServ.getUserDetailsByRole(loginDto);
		
		System.out.println("UserDetails: "+user.getEmail()+user.getName()+user.getToken()+user.getType()+user.getId());

		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();

		jwtAuthResponse.setUser(user);

		HttpSession session = request.getSession();

		session.setAttribute("usernameoremail", loginDto.getUsernameOrEmail());

		return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session != null) {
			session.invalidate(); // destroy session
		}

		return ResponseEntity.ok("Logged out successfully");
	}
}
