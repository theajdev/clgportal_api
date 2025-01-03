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
import com.aj.clgportal.dto.UserTypeDto;
import com.aj.clgportal.service.UserTypeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserTypeController {

	@Autowired
	public UserTypeService userTypeServ;

	@PostMapping("/")
	public ResponseEntity<UserTypeDto> NewUserType(@Valid @RequestBody UserTypeDto userTypeDto) {
		UserTypeDto userType = userTypeServ.createUserType(userTypeDto);
		return new ResponseEntity<UserTypeDto>(userType, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserTypeDto> updateUserType(@Valid @RequestBody UserTypeDto userTypeDto, @PathVariable long id) {
		UserTypeDto updatedUserType = userTypeServ.updateUserType(userTypeDto, id);
		return ResponseEntity.ok(updatedUserType);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteUserType(@PathVariable long id) {
		userTypeServ.deleteUserType(id);
		return new ResponseEntity<>(new ApiResponse("User type deleted successfully.", true), HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<UserTypeDto>> getAllUserTypes(){
		List<UserTypeDto> list = userTypeServ.getAllUserTypes();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserTypeDto> getUserType(@PathVariable long id){
		UserTypeDto userType = userTypeServ.getUserTypeById(id);
		return new ResponseEntity<>(userType,HttpStatus.OK);
	}
}
