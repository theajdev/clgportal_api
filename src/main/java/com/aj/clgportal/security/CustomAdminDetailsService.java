package com.aj.clgportal.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.aj.clgportal.entity.Admin;
import com.aj.clgportal.entity.Student;
import com.aj.clgportal.entity.Teacher;
import com.aj.clgportal.exception.UserNameNotFoundException;
import com.aj.clgportal.repository.AdminRepository;
import com.aj.clgportal.repository.StudentRepository;
import com.aj.clgportal.repository.TeacherRepository;

@Service
public class CustomAdminDetailsService implements UserDetailsService {

	@Autowired
	private TeacherRepository teacherRepo;

	@Autowired
	private AdminRepository adminRepo;

	@Autowired
	private StudentRepository studRepo;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UserNameNotFoundException {

		// Attempt to find the admin by username or email
		Admin admin = adminRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

		if (admin != null) {
			// If found, return the UserDetails for admin
			List<SimpleGrantedAuthority> authorities = admin.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role.getRoleDesc())).collect(Collectors.toList());
			return new User(usernameOrEmail, admin.getPassword(), authorities);
		}

		// Attempt to find the teacher by username or email
		Teacher teacher = teacherRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

		if (teacher != null) {

			// If found, return the UserDetails for teacher
			List<SimpleGrantedAuthority> authorities = teacher.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role.getRoleDesc())).collect(Collectors.toList());
			return new User(usernameOrEmail, teacher.getPassword(), authorities);
		}

		// Attempt to find the student by username or email
		Student student = studRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

		if (student != null) {

			// If found, return the UserDetails for teacher
			List<SimpleGrantedAuthority> authorities = student.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role.getRoleDesc())).collect(Collectors.toList());
			return new User(usernameOrEmail, student.getPassword(), authorities);
		}
		
		// If neither admin nor teacher are found, throw the custom exception
		throw new UserNameNotFoundException("Invalid username or email.");
	}

}
