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

    AuthController(ModelMapper ModelMapper, DepartmentController departmentController) {
        this.ModelMapper = ModelMapper;
        this.departmentController = departmentController;
    }

	// build Login REST API
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto,HttpServletRequest request) {
		
		UserResponseDto user = authServ.getUserDetailsByRole(loginDto);
		
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		
		jwtAuthResponse.setUser(user);
		
		HttpSession session=request.getSession();
		session.setAttribute("usernameoremail",loginDto.getUsernameOrEmail());
		
		return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
	}
}
