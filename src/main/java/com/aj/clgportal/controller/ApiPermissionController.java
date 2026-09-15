package com.aj.clgportal.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aj.clgportal.dto.PermissionDto;
import com.aj.clgportal.entity.ApiPermission;
import com.aj.clgportal.entity.Role;
import com.aj.clgportal.repository.ApiPermissionRepository;
import com.aj.clgportal.repository.RoleRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Tag(name = "API Permissions APIs",description = "Get & update API permissions.")
public class ApiPermissionController {

	private final ApiPermissionRepository permissionRepo;
	private final RoleRepository roleRepo;

	@GetMapping("/permissions")
	public List<ApiPermission> getAllPermissions() {
		return permissionRepo.findAll();
	}

	@PutMapping("/role/{roleId}")
	public ResponseEntity<String> updateSavedPermissions(@PathVariable Long roleId,
			@RequestBody List<PermissionDto> permissions) {

		Role role = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));

		List<Long> selectedIds = permissions.stream().filter(PermissionDto::isSelected).map(PermissionDto::getId)
				.toList();

		selectedIds.forEach(ids -> {
			System.out.println("selectedIds: " + ids);
		});

		Set<ApiPermission> selectedPermissions = Set.copyOf(permissionRepo.findAllById(selectedIds));

		selectedPermissions.forEach(ids -> {
			System.out.println("selectedPermissions: " + ids.getApiUrl() + "" + ids.getHttpMethod() + "" + ids.getId());
		});

		role.setPermissions(selectedPermissions.stream().map(permission -> permission).collect(Collectors.toSet()));

		roleRepo.save(role);

		return ResponseEntity.ok("Permissions updated");
	}

	@GetMapping("/role/{roleId}")
	public List<PermissionDto> getRolePermissions(@PathVariable Long roleId) {

		Role role = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));

		Set<Long> assignedPermissionIds = role.getPermissions().stream().map(ApiPermission::getId)
				.collect(Collectors.toSet());

		return permissionRepo.findAll().stream().map(permission -> {

			PermissionDto dto = new PermissionDto();

			dto.setId(permission.getId());
			dto.setApiUrl(permission.getApiUrl());
			dto.setHttpMethod(permission.getHttpMethod());

			dto.setSelected(assignedPermissionIds.contains(permission.getId()));

			return dto;
		}).toList();

	}
}
