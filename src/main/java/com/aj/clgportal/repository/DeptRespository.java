package com.aj.clgportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aj.clgportal.entity.Department;

public interface DeptRespository extends JpaRepository<Department, Long> {

	boolean existsByDeptDesc(String deptDesc);
}
