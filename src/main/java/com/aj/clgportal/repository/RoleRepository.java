package com.aj.clgportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aj.clgportal.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	@Query(value="select * from tbl_role where role_desc=?1",nativeQuery = true)
	Role findByName(String name);
	
	List<Role> findByStatus(Character str);
	
	@Query(value = "select COALESCE(max(r.role_id),0) from tbl_role r",nativeQuery = true)
	Long findMaxRoleId();
	
	boolean existsByRoleDesc(String roleDesc);
}