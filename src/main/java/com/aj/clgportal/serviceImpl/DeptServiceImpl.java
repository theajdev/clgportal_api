package com.aj.clgportal.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.DepartmentDto;
import com.aj.clgportal.entity.Department;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.DeptRespository;
import com.aj.clgportal.service.DeptService;

@Service
public class DeptServiceImpl implements DeptService {
	
	@Autowired
	DeptRespository deptRepo;
	
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public DepartmentDto newDepartment(DepartmentDto deptDto) {
		Department dept=new Department();
		dept.setDeptDesc(deptDto.getDeptDesc());
		dept.setStatus(deptDto.getStatus());
		Department newDept = deptRepo.save(dept);
		DepartmentDto newDeptDto = deptToDto(newDept);
		return newDeptDto;
	}

	@Override
	public DepartmentDto updateDepartment(DepartmentDto deptDto, long id) {
		Department dept = deptRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Department", "department id", id));
		dept.setDeptDesc(deptDto.getDeptDesc());
		dept.setStatus(deptDto.getStatus());
		Department newDept = deptRepo.save(dept);
		DepartmentDto newDeptDto = deptToDto(newDept);
		return newDeptDto;
	}

	@Override
	public void deleteDepartment(long id) {
		Department dept = deptRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Department", "department id", id));
		deptRepo.delete(dept);
	}

	@Override
	public DepartmentDto getDepartmentById(long id) {
		Department dept = deptRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Department", "department id", id));
		DepartmentDto deptDto = deptToDto(dept);
		return deptDto;
	}

	@Override
	public List<DepartmentDto> getAllDepartments() {
		List<Department> depts = deptRepo.findAll();
		List<DepartmentDto> lst = depts.stream().map((list)->deptToDto(list)).collect(Collectors.toList());
		return lst;
	}
	
	public DepartmentDto deptToDto(Department dept) {
		DepartmentDto deptDto = modelMapper.map(dept, DepartmentDto.class);
		return deptDto;
	}
	
	public Department dtoToDept(DepartmentDto deptDto) {
		Department dept = modelMapper.map(deptDto, Department.class);
		return dept;
	}

}
