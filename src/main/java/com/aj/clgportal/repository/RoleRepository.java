package com.aj.clgportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.aj.clgportal.entity.Role;

import jakarta.transaction.Transactional;

public interface RoleRepository extends JpaRepository<Role, Long> {
	@Query(value="select * from tbl_role where role_desc=?1",nativeQuery = true)
	Role findByName(String name);
	
	// Query to reset the sequence, called after role deletion
	// Query to reset the sequence, called after role deletion
	// Reset the sequence without expecting a result
    @Modifying
    @Transactional
    @Query(value = "SELECT pg_catalog.setval('tbl_role_seq', :value, false);", nativeQuery = true)
    int resetRoleSequence(long value);

    // Get the maximum ID from the roles
    @Query("SELECT MAX(r.id) FROM Role r")
    Long getMaxRoleId();
}