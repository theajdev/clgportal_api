package com.aj.clgportal.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.UserTypeDto;
import com.aj.clgportal.entity.UserType;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.UserTypeRepository;
import com.aj.clgportal.service.UserTypeService;

@Service
public class UserTypeServiceImpl implements UserTypeService {

	@Autowired
	UserTypeRepository userTypeRepo;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public UserTypeDto createUserType(UserTypeDto userTypeDto) {
		UserType userType = new UserType();
		userType.setUserDesc(userTypeDto.getUserDesc());
		userType.setStatus(userTypeDto.getStatus());
		UserType save = userTypeRepo.save(userType);
		UserTypeDto newUserType = UserTypeToDto(save);
		return newUserType;
	}

	@Override
	public UserTypeDto updateUserType(UserTypeDto userTypeDto, long id) {
		UserType userType = userTypeRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User type", "id", id));
		userType.setUserDesc(userTypeDto.getUserDesc());
		userType.setStatus(userTypeDto.getStatus());
		UserType updatedUserType = userTypeRepo.save(userType);
		UserTypeDto usertype = UserTypeToDto(updatedUserType);
		return usertype;
	}

	@Override
	public void deleteUserType(long id) {
		UserType userType = userTypeRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User type", "id", id));
		userTypeRepo.delete(userType);

	}

	@Override
	public UserTypeDto getUserTypeById(long id) {
		UserType userType = userTypeRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User type", "id", id));
		UserTypeDto userTypeDto = UserTypeToDto(userType);
		return userTypeDto;
	}

	@Override
	public List<UserTypeDto> getAllUserTypes() {
		List<UserType> list = userTypeRepo.findAll();
		List<UserTypeDto> lst = list.stream().map(users -> UserTypeToDto(users)).collect(Collectors.toList());
		return lst;
	}

	public UserTypeDto UserTypeToDto(UserType userType) {
		UserTypeDto userTypeDto = modelMapper.map(userType, UserTypeDto.class);
		return userTypeDto;
	}

	public UserType DtoToUserType(UserTypeDto userTypeDto) {
		UserType userType = modelMapper.map(userTypeDto, UserType.class);
		return userType;
	}

}
