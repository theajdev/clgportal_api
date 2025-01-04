package com.aj.clgportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aj.clgportal.dto.AdminDto;
import com.aj.clgportal.dto.ApiResponse;
import com.aj.clgportal.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@PostMapping("/")
	public ResponseEntity<AdminDto> newAdmin(@RequestBody AdminDto adminDto){
		AdminDto newAdmin = adminService.newAdmin(adminDto);
		return new ResponseEntity<AdminDto>(newAdmin,HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<AdminDto> updateAdmin(@RequestBody AdminDto adminDto, @PathVariable long id){
		AdminDto updatedAdmin = adminService.updateAdmin(adminDto, id);
		return ResponseEntity.ok(updatedAdmin);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteAdmin(@PathVariable long id){
		adminService.deleteAdmin(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Admin deleted successfully.", false),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AdminDto> getAdminDetailsById(@PathVariable long id){
		AdminDto admin = adminService.getAdminDetailsById(id);
		return new ResponseEntity<AdminDto>(admin,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<AdminDto>> getAllAdmins(){
		List<AdminDto> list = adminService.getAllAdminsList();
		return ResponseEntity.ok(list);
	}
}
