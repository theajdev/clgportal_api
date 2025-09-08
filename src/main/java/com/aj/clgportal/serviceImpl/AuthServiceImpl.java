package com.aj.clgportal.serviceImpl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.LoginDto;
import com.aj.clgportal.dto.UserResponseDto;
import com.aj.clgportal.entity.Admin;
import com.aj.clgportal.entity.Student;
import com.aj.clgportal.entity.Teacher;
import com.aj.clgportal.exception.UserNameNotFoundException;
import com.aj.clgportal.repository.AdminRepository;
import com.aj.clgportal.repository.StudentRepository;
import com.aj.clgportal.repository.TeacherRepository;
import com.aj.clgportal.security.JwtTokenProvider;
import com.aj.clgportal.service.AuthService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private AuthenticationManager authenticationManager;
	
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Autowired
	private TeacherRepository teacherRepo;
	
	@Autowired
	private StudentRepository studentRepo;

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

	@Override
	public String getAuthority(LoginDto loginDto) {
		StringBuilder authorityBuilder = new StringBuilder();
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()));
		Collection<? extends GrantedAuthority> authorities = authenticate.getAuthorities();
		
		 for (GrantedAuthority authority : authorities) {
		        authorityBuilder.append(authority.getAuthority());
		    }
		
		return authorityBuilder.toString();
	}
	
	public UserResponseDto getUserDetailsByRole(LoginDto loginDto) {
	    String usernameOrEmail = loginDto.getUsernameOrEmail();
	    String authority = getAuthority(loginDto); // "ROLE_ADMIN", "ROLE_TEACHER", etc.
	    String token=login(loginDto);
	    UserResponseDto dto = new UserResponseDto();

	    switch (authority) {
	        case "ROLE_ADMIN":
	            Admin admin = adminRepo.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
	            dto.setId(admin.getId());
	            dto.setName(admin.getUsername());
	            dto.setEmail(admin.getEmail());
	            dto.setType("ADMIN");
	            dto.setToken(token);
	            break;

	        case "ROLE_TEACHER":
	            Teacher teacher = teacherRepo.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
	            dto.setId(teacher.getId());
	            dto.setName(teacher.getUsername());
	            dto.setEmail(teacher.getEmail());
	            dto.setType("TEACHER");
	            dto.setToken(token);
	            break;

	        case "ROLE_STUDENT":
	            Student student = studentRepo.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
	            dto.setId(student.getId());
	            dto.setName(student.getUsername());
	            dto.setEmail(student.getEmail());
	            dto.setType("STUDENT");
	            dto.setToken(token);
	            break;

	        default:
	            throw new RuntimeException("Unknown role: " + authority);
	    }

	    return dto;
	}

}
