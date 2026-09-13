package com.aj.clgportal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aj.clgportal.dto.ApiResponse;
import com.aj.clgportal.dto.RoleDetailProjection;
import com.aj.clgportal.dto.RoleDto;
import com.aj.clgportal.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

	public final RoleService roleService;
	
	@PostMapping("/")
	public ResponseEntity<RoleDto> NewUserType(@Valid @RequestBody RoleDto roleDto) {
		RoleDto userType = roleService.createUserType(roleDto);
		return new ResponseEntity<RoleDto>(userType, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<RoleDto> updateUserType(@Valid @RequestBody RoleDto roleDto, @PathVariable long id) {
		RoleDto updatedUserType = roleService.updateUserType(roleDto, id);
		return ResponseEntity.ok(updatedUserType);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteUserType(@PathVariable long id) {
		roleService.deleteUserType(id);
		Long maxRoleId = roleService.getMaxRoleId();
		roleService.resetRoleSequence(maxRoleId + 1);
		return new ResponseEntity<>(new ApiResponse("User type deleted successfully.", true), HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<RoleDto>> getAllUserTypes() {
		List<RoleDto> list = roleService.getAllUserTypes();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoleDto> getUserType(@PathVariable long id) {
		RoleDto userType = roleService.getUserTypeById(id);
		return new ResponseEntity<>(userType, HttpStatus.OK);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<RoleDto>> getUserType(@PathVariable Boolean status) {
		List<RoleDto> userType = roleService.getRolesByStatus(status);
		return ResponseEntity.ok(userType);
	}

	@GetMapping("/count")
	public ResponseEntity<Long> getRoleCount() {
		Long roleCount = roleService.getRoleCount();
		return ResponseEntity.ok(roleCount);
	}
	
	@GetMapping("/details")
	public ResponseEntity<List<RoleDetailProjection>> getUsersByRoleId(@RequestParam(defaultValue = "1") long roleId){
		List<RoleDetailProjection> users = roleService.getRoleDetails(roleId);
		return ResponseEntity.ok(users);
	}
}
