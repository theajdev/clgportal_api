package com.aj.clgportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aj.clgportal.entity.ApiPermission;

public interface ApiPermissionRepository extends JpaRepository<ApiPermission, Long> {
	   boolean existsByApiUrlAndHttpMethod(
	            String apiUrl,
	            String httpMethod);
}
