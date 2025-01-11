package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.dto.DepartmentDto;

public interface DeptService {
	public DepartmentDto newDepartment(DepartmentDto deptDto);
	public DepartmentDto updateDepartment(DepartmentDto deptDto,long id);
	public void deleteDepartment(long id);
	public DepartmentDto getDepartmentById(long id);
	public List<DepartmentDto> getAllDepartments();
}
