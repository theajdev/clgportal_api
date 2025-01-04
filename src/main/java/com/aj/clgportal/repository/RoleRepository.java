package com.aj.clgportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aj.clgportal.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	@Query(value="select * from tbl_role where role_desc=?1",nativeQuery = true)
	Role findByName(String name);
}
