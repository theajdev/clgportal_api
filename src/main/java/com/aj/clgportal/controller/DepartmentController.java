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
import com.aj.clgportal.dto.DepartmentDto;
import com.aj.clgportal.service.DeptService;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

	@Autowired
	DeptService deptServ;

	@PostMapping("/")
	public ResponseEntity<DepartmentDto> newDepartment(@RequestBody DepartmentDto deptDto) {
		DepartmentDto newDepartment = deptServ.newDepartment(deptDto);
		return new ResponseEntity<DepartmentDto>(newDepartment, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DepartmentDto> updateDepartment(@RequestBody DepartmentDto deptDto, @PathVariable long id) {
		DepartmentDto updatedDepartment = deptServ.updateDepartment(deptDto, id);
		return ResponseEntity.ok(updatedDepartment);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteDepartment(@PathVariable long id) {
		deptServ.deleteDepartment(id);
		Long maxDeptId = deptServ.getMaxDeptId();
		deptServ.resetDeptSequence(maxDeptId+1);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Department deleted successfully.", true),
				HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable long id) {
		DepartmentDto dept = deptServ.getDepartmentById(id);
		return new ResponseEntity<DepartmentDto>(dept, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
		List<DepartmentDto> allDepartments = deptServ.getAllDepartments();
		return ResponseEntity.ok(allDepartments);
	}
	
	@GetMapping("/status/{status}")
	public ResponseEntity<List<DepartmentDto>> getDeptByStatus(@PathVariable Character status){
		List<DepartmentDto> depts = deptServ.getDeptByStatus(status);
		return ResponseEntity.ok(depts);
	}
}
