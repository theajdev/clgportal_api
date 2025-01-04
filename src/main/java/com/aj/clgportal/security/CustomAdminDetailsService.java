package com.aj.clgportal.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aj.clgportal.entity.Admin;
import com.aj.clgportal.repository.AdminRepository;

@Service
public class CustomAdminDetailsService implements UserDetailsService {
	
	@Autowired
	private AdminRepository adminRepo;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		Admin admin = adminRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
		.orElseThrow(()-> new UsernameNotFoundException("Admin not exists by username or email."));
		
		List<SimpleGrantedAuthority> authorities = admin.getRoles().stream().map((role)-> new SimpleGrantedAuthority(role.getRoleDesc())).collect(Collectors.toList());
		
		return new User(usernameOrEmail,admin.getPassword(),authorities);
	}

}
