package com.aj.clgportal.serviceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.RoleDetailsProjection;
import com.aj.clgportal.dto.RoleDto;
import com.aj.clgportal.entity.Role;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.exception.DuplicateResourceException;
import com.aj.clgportal.repository.RoleRepository;
import com.aj.clgportal.service.RoleService;

import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public RoleDto createUserType(RoleDto roleDto) {
		String roleDesc = "ROLE_" + roleDto.getRoleDesc().toUpperCase();
		if (roleRepo.existsByRoleDesc(roleDesc)) {
			throw new DuplicateResourceException(roleDto.getRoleDesc() + " user type already exists.");
		} else if (roleDesc.contains("ROLE_ROLE_")) {
			throw new DuplicateResourceException("ROLE_ is not allowed.");
		} else {
			Role role = new Role();
			role.setRoleDesc(roleDesc);
			role.setRoleDisp(roleDto.getRoleDesc().toUpperCase());
			role.setStatus(roleDto.getStatus());
			Role save = roleRepo.save(role);
			RoleDto newUserType = UserTypeToDto(save);
			return newUserType;
		}
	}

	@Override
	public RoleDto updateUserType(RoleDto roleDto, long id) {
		String roleDesc = "ROLE_" + roleDto.getRoleDesc().toUpperCase();
		if (roleDesc.contains("ROLE_ROLE_")) {
			throw new DuplicateResourceException("ROLE_ is not allowed.");
		} else {

			Role role = roleRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("User type", "id", id));
			role.setRoleDesc(roleDesc);
			role.setRoleDisp(roleDto.getRoleDesc().toUpperCase());
			role.setStatus(roleDto.getStatus());
			Role updatedUserType = roleRepo.save(role);
			RoleDto usertype = UserTypeToDto(updatedUserType);
			return usertype;
		}
	}

	@Override
	public void deleteUserType(long id) {
		Role role = roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User type", "id", id));
		roleRepo.delete(role);
	}

	@Override
	public RoleDto getUserTypeById(long id) {
		Role role = roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User type", "id", id));
		RoleDto roleDto = UserTypeToDto(role);
		return roleDto;
	}

	@Override
	public List<RoleDto> getAllUserTypes() {
		List<Role> list = roleRepo.findAll();
		List<RoleDto> lst = list.stream().map(users -> UserTypeToDto(users)).collect(Collectors.toList());
		lst.sort(Comparator.comparing(RoleDto::getId));
		return lst;
	}

	@Override
	public List<RoleDto> getUserTypesByStatus(Character str) {
		List<Role> list = roleRepo.findByStatus(str);
		List<RoleDto> lst = list.stream().map(users -> UserTypeToDto(users)).collect(Collectors.toList());
		lst.sort(Comparator.comparing(RoleDto::getId));
		return lst;
	}

	@Override
	public Long getMaxRoleId() {
		Long maxRoleId = roleRepo.findMaxRoleId();
		return maxRoleId;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	@Override
	public void resetRoleSequence(Long nextVal) {
		String sql = "ALTER SEQUENCE tbl_role_seq RESTART WITH " + nextVal;

		entityManager.createNativeQuery(sql).executeUpdate();
	}

	public RoleDto UserTypeToDto(Role role) {
		RoleDto roleDto = modelMapper.map(role, RoleDto.class);
		return roleDto;
	}

	public Role DtoToUserType(RoleDto roleDto) {
		Role role = modelMapper.map(roleDto, Role.class);
		return role;
	}

	@Override
	public Long getRoleCount(Character status) {
		Long RoleCount = roleRepo.countByStatus(status);
		return RoleCount;
	}

	@Override
	public List<RoleDetailsProjection> getRoleDetails(Integer roleId) {
		return roleRepo.getRoleDetails(roleId);
	}
}
