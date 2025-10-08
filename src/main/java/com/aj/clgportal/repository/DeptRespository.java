package com.aj.clgportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aj.clgportal.entity.Department;

public interface DeptRespository extends JpaRepository<Department, Long> {

	List<Department> findByStatus(Character status);
	
	boolean existsByDeptDesc(String deptDesc);
	
	@Query(value = "select COALESCE(max(d.dept_id),0) from tbl_department d",nativeQuery = true)
	Long findMaxDeptId();
	
	Long countByStatus(Character status);
}
