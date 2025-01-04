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

import com.aj.clgportal.dto.ApiResponse;
import com.aj.clgportal.dto.RoleDto;
import com.aj.clgportal.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/role")
public class RoleController {

	@Autowired
	public RoleService userTypeServ;

	@PostMapping("/")
	public ResponseEntity<RoleDto> NewUserType(@Valid @RequestBody RoleDto roleDto) {
		RoleDto userType = userTypeServ.createUserType(roleDto);
		return new ResponseEntity<RoleDto>(userType, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<RoleDto> updateUserType(@Valid @RequestBody RoleDto roleDto, @PathVariable long id) {
		RoleDto updatedUserType = userTypeServ.updateUserType(roleDto, id);
		return ResponseEntity.ok(updatedUserType);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteUserType(@PathVariable long id) {
		userTypeServ.deleteUserType(id);
		return new ResponseEntity<>(new ApiResponse("User type deleted successfully.", true), HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<RoleDto>> getAllUserTypes(){
		List<RoleDto> list = userTypeServ.getAllUserTypes();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RoleDto> getUserType(@PathVariable long id){
		RoleDto userType = userTypeServ.getUserTypeById(id);
		return new ResponseEntity<>(userType,HttpStatus.OK);
	}
}
