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
import com.aj.clgportal.entity.Department;
import com.aj.clgportal.entity.Role;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.AdminRepository;
import com.aj.clgportal.repository.DeptRespository;
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
	private final DeptRespository deptRepo;

	@Override
	public AdminDto newAdmin(AdminDto adminDto) {
		
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String formattedDate = formatter.format(currentDate);

		Date postedDate = null;
		try {
			postedDate = formatter.parse(formattedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Department department = deptRepo.findById(adminDto.getDeptId()).orElseThrow(
				() -> new ResourceNotFoundException("Department", "department id", adminDto.getDeptId()));
		
		Admin admin=new Admin();
		admin.setFirstName(adminDto.getFirstName());
		admin.setMiddleName(adminDto.getMiddleName());
		admin.setLastName(adminDto.getLastName());
		admin.setUsername(adminDto.getUsername());
		admin.setMobileNo(adminDto.getMobileNo());
	    admin.setAddress(adminDto.getAddress());
		admin.setAbout(adminDto.getAbout());
		admin.setDesignation(adminDto.getDesignation());
		admin.setPostedOn(postedDate);
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
        admin.setDepts(department);
		adminDto.setDeptId(department.getId());
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
		
		Department department = deptRepo.findById(adminDto.getDeptId()).orElseThrow(
				() -> new ResourceNotFoundException("Department", "department id", adminDto.getDeptId()));
		
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
		admin.setDepts(department);
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
		adminDto.setDeptId(admin.getDepts().getId());
		return adminDto;
	}

	@Override
	public List<AdminDto> getAllAdminsList() {
		return adminRepo.findAll().stream()
	            .map(teacher -> {
	                AdminDto dto = adminToDto(teacher);
	                dto.setDeptId(teacher.getDepts().getId());
	                return dto;
	            })
	            .collect(Collectors.toList());
	}
	
	@Override
	public Long getAdminCount(Character status) {
		return adminRepo.countByStatus(status);
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
