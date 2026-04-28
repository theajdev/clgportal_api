package com.aj.clgportal.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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
	
	private final AdminRepository adminRepo;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

	@Override
	public AdminDto newAdmin(AdminDto adminDto) {
		Admin admin=new Admin();
		admin.setFirstName(adminDto.getFirstName());
		admin.setMiddleName(adminDto.getMiddleName());
		admin.setLastName(adminDto.getLastName());
		admin.setUsername(adminDto.getUsername());
		admin.setMobileNo(adminDto.getMobileNo());
	    admin.setAddress(adminDto.getAddress());
		admin.setAbout(adminDto.getAbout());
		admin.setDesignation(adminDto.getDesignation());
		admin.setPostedOn(adminDto.getPostedOn());
		admin.setEmail(adminDto.getEmail());
		if (adminDto.getPassword() != null && !adminDto.getPassword().isEmpty()) {
		    admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
		}
		admin.setProfilePic(adminDto.getProfilePic());
		admin.setStatus(adminDto.getStatus());
		List<Role> roles = new ArrayList<>();
        Role userRole = roleRepo.findByName("ROLE_ADMIN");
        if (userRole == null) {
            throw new RuntimeException("ROLE_ADMIN not found");
        }
        roles.add(userRole);
        admin.setRoles(roles);
		Admin newAdmin = adminRepo.save(admin);
		AdminDto newAdminDto = adminToDto(newAdmin);
		return newAdminDto;
	}

	@Override
	public AdminDto updateAdmin(AdminDto adminDto, long id) {
		
		Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = formatter.format(currentDate);
        
        Date updatedDate=null;
		try {
			updatedDate = formatter.parse(formattedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    Admin admin = adminRepo.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Admin", "admin id", id));

	    admin.setFirstName(adminDto.getFirstName());
	    admin.setMiddleName(adminDto.getMiddleName());
	    admin.setLastName(adminDto.getLastName());
	    admin.setUsername(adminDto.getUsername());
	    admin.setMobileNo(adminDto.getMobileNo());
	    admin.setAddress(adminDto.getAddress());
	    admin.setEmail(adminDto.getEmail());
	    admin.setAbout(adminDto.getAbout());
		admin.setDesignation(adminDto.getDesignation());
		admin.setPostedOn(adminDto.getPostedOn());
		admin.setUpdatedOn(updatedDate);
		/*
		 * if (adminDto.getPassword() != null && !adminDto.getPassword().isEmpty()) {
		 * admin.setPassword(passwordEncoder.encode(adminDto.getPassword())); }
		 */

	    admin.setProfilePic(adminDto.getProfilePic());
	    admin.setStatus(adminDto.getStatus());

	    List<Role> roles = new ArrayList<>();
		Role userRole = roleRepo.findByName("ROLE_ADMIN");
		roles.add(userRole);
		admin.setRoles(roles);

	    Admin updatedAdmin = adminRepo.save(admin);
	    return adminToDto(updatedAdmin);
	}

	@Override
	public void deleteAdmin(long id) {
		Admin admin = adminRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Admin", "admin id", id));
		adminRepo.delete(admin);
	}

	@Override
	public AdminDto getAdminDetailsById(long id) {
		Admin admin = adminRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Admin", "admin id", id));
		AdminDto adminDto = adminToDto(admin);
		return adminDto;
	}

	@Override
	public List<AdminDto> getAllAdminsList() {
		List<Admin> allAdmins = adminRepo.findAll();
		List<AdminDto> list = allAdmins.stream().map((admin)->adminToDto(admin)).collect(Collectors.toList());
		return list;
	}
	
	public AdminDto adminToDto(Admin admin) {
		AdminDto adminDto = modelMapper.map(admin, AdminDto.class);
		return adminDto;
	}
	
	public Admin dtoToAdmin(AdminDto adminDto) {
		Admin admin = modelMapper.map(adminDto, Admin.class);
		return admin;
	}

}
