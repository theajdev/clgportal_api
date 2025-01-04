package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.dto.RoleDto;

public interface RoleService {
	public RoleDto createUserType(RoleDto roleDto);
	public RoleDto updateUserType(RoleDto roleDto,long id);
	public void deleteUserType(long id);
	public RoleDto getUserTypeById(long id);
	public List<RoleDto> getAllUserTypes();
}
