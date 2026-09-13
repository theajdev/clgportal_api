package com.aj.clgportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aj.clgportal.dto.RoleDetailProjection;
import com.aj.clgportal.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRoleName(String roleName);

	Boolean existsByRoleName(String roleName);

	@Query(value = "select COALESCE(max(r.role_id),0) from tbl_role r", nativeQuery = true)
	Long findMaxRoleId();

	
	long countByIsActive(Boolean isActive);
	
	List<Role> getRolesByIsActive(Boolean isActive);
	
	 @Query(value = """
	            SELECT *
	            FROM public.get_role_details(:roleId)
	            """, nativeQuery = true)
	    List<RoleDetailProjection> getRoleDetails(Long roleId);
}