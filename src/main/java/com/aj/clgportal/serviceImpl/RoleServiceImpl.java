package com.aj.clgportal.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.RoleDto;
import com.aj.clgportal.entity.Role;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.RoleRepository;
import com.aj.clgportal.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository userTypeRepo;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public RoleDto createUserType(RoleDto roleDto) {
		Role role = new Role();
		role.setRoleDesc(roleDto.getRoleDesc());
		role.setStatus(roleDto.getStatus());
		Role save = userTypeRepo.save(role);
		RoleDto newUserType = UserTypeToDto(save);
		return newUserType;
	}

	@Override
	public RoleDto updateUserType(RoleDto roleDto, long id) {
		Role role = userTypeRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User type", "id", id));
		role.setRoleDesc(roleDto.getRoleDesc());
		role.setStatus(roleDto.getStatus());
		Role updatedUserType = userTypeRepo.save(role);
		RoleDto usertype = UserTypeToDto(updatedUserType);
		return usertype;
	}

	@Override
	public void deleteUserType(long id) {
		Role role = userTypeRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User type", "id", id));
		userTypeRepo.delete(role);

	}

	@Override
	public RoleDto getUserTypeById(long id) {
		Role role = userTypeRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User type", "id", id));
		RoleDto roleDto = UserTypeToDto(role);
		return roleDto;
	}

	@Override
	public List<RoleDto> getAllUserTypes() {
		List<Role> list = userTypeRepo.findAll();
		List<RoleDto> lst = list.stream().map(users -> UserTypeToDto(users)).collect(Collectors.toList());
		return lst;
	}

	public RoleDto UserTypeToDto(Role role) {
		RoleDto roleDto = modelMapper.map(role, RoleDto.class);
		return roleDto;
	}

	public Role DtoToUserType(RoleDto roleDto) {
		Role role = modelMapper.map(roleDto, Role.class);
		return role;
	}

}
