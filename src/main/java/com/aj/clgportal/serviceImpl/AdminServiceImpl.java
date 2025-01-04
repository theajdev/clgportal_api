package com.aj.clgportal.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.AdminDto;
import com.aj.clgportal.entity.Admin;
import com.aj.clgportal.entity.Role;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.AdminRepository;
import com.aj.clgportal.repository.RoleRepository;
import com.aj.clgportal.service.AdminService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminRepository adminRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	RoleRepository roleRepo;
	
	private PasswordEncoder passwordEncoder;

	@Override
	public AdminDto newAdmin(AdminDto adminDto) {
		Admin admin=new Admin();
		admin.setFirstName(adminDto.getFirstName());
		admin.setMiddleName(adminDto.getMiddleName());
		admin.setLastName(adminDto.getLastName());
		admin.setUsername(adminDto.getUsername());
		admin.setEmail(adminDto.getEmail());
		admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
		admin.setProfilePic(adminDto.getProfilePic());
		admin.setStatus(adminDto.getStatus());
		List<Role> roles = new ArrayList<>();
        Role userRole = roleRepo.findByName("ROLE_ADMIN");
        roles.add(userRole);
        admin.setRoles(roles);
		Admin newAdmin = adminRepo.save(admin);
		AdminDto newAdminDto = AdminToDto(newAdmin);
		return newAdminDto;
	}

	@Override
	public AdminDto updateAdmin(AdminDto adminDto, long id) {
		Admin admin = adminRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Admin", "admin id", id));
		admin.setFirstName(adminDto.getFirstName());
		admin.setMiddleName(adminDto.getMiddleName());
		admin.setLastName(adminDto.getLastName());
		admin.setUsername(adminDto.getUsername());
		admin.setEmail(adminDto.getEmail());
		admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
		admin.setProfilePic(adminDto.getProfilePic());
		admin.setStatus(adminDto.getStatus());
		List<Role> roles = new ArrayList<>();
        Role userRole = roleRepo.findByName("ROLE_ADMIN");
        roles.add(userRole);
        admin.setRoles(roles);
		Admin updatedAdmin = adminRepo.save(admin);
		AdminDto updateAdminDto = AdminToDto(updatedAdmin);
		return updateAdminDto;
	}

	@Override
	public void deleteAdmin(long id) {
		Admin admin = adminRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Admin", "admin id", id));
		adminRepo.delete(admin);
	}

	@Override
	public AdminDto getAdminDetailsById(long id) {
		Admin admin = adminRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Admin", "admin id", id));
		AdminDto adminDto = AdminToDto(admin);
		return adminDto;
	}

	@Override
	public List<AdminDto> getAllAdminsList() {
		List<Admin> allAdmins = adminRepo.findAll();
		List<AdminDto> list = allAdmins.stream().map((admin)->AdminToDto(admin)).collect(Collectors.toList());
		return list;
	}

	
	public AdminDto AdminToDto(Admin admin) {
		AdminDto adminDto = modelMapper.map(admin, AdminDto.class);
		return adminDto;
	}
	
	public Admin DtoToAdmin(AdminDto adminDto) {
		Admin admin = modelMapper.map(adminDto, Admin.class);
		return admin;
	}

}
