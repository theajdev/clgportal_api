package com.aj.clgportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aj.clgportal.dto.RoleDetailsProjection;
import com.aj.clgportal.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	@Query(value = "select * from tbl_role where role_desc=?1", nativeQuery = true)
	Role findByName(String name);

	List<Role> findByStatus(Character str);

	@Query(value = "select COALESCE(max(r.role_id),0) from tbl_role r", nativeQuery = true)
	Long findMaxRoleId();

	boolean existsByRoleDesc(String roleDesc);

	Long countByStatus(Character status);

	@Query(value = """
			SELECT
			id AS id,
			    role_id AS roleId,
			    profile_pic AS profilePic,
			    first_name AS firstName,
			    last_name AS lastName,
			    email AS email
			FROM get_role_details(:roleId)
			""", nativeQuery = true)
	List<RoleDetailsProjection> getRoleDetails(@Param("roleId") Integer roleId);
}