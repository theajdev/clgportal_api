package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.dto.UserTypeDto;

public interface UserTypeService {
	public UserTypeDto createUserType(UserTypeDto userTypeDto);
	public UserTypeDto updateUserType(UserTypeDto userTypeDto,long id);
	public void deleteUserType(long id);
	public UserTypeDto getUserTypeById(long id);
	public List<UserTypeDto> getAllUserTypes();
}
