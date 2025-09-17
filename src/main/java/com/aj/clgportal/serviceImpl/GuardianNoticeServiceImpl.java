package com.aj.clgportal.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.GuardianNoticeDto;
import com.aj.clgportal.entity.GuardianNotice;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.GuardianNoticeRepos;
import com.aj.clgportal.repository.StudentRepository;
import com.aj.clgportal.service.GuardianNoticeService;

@Service
public class GuardianNoticeServiceImpl implements GuardianNoticeService {
	
	@Autowired
	GuardianNoticeRepos guardianNoticeRepos;
	
	@Autowired
	StudentRepository studentRepo;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public GuardianNoticeDto newGuardianNotice(GuardianNoticeDto guardianNoticeDto) {
		GuardianNotice guardianNotice=new GuardianNotice();
		guardianNotice.setNoticeTitle(guardianNoticeDto.getNoticeTitle());
		guardianNotice.setNoticeDesc(guardianNoticeDto.getNoticeDesc());
		guardianNotice.setCreationDate(guardianNoticeDto.getCreationDate());
		guardianNotice.setStatus(guardianNoticeDto.getStatus());
		GuardianNotice newNotice = guardianNoticeRepos.save(guardianNotice);
		GuardianNoticeDto guardianNoticeToDto = GuardianNoticeToDto(newNotice);	
		return guardianNoticeToDto;
	}

	@Override
	public GuardianNoticeDto updateGuardianNotice(GuardianNoticeDto guardianNoticeDto, long id) {
		GuardianNotice guardianNotice = guardianNoticeRepos.findById(id).orElseThrow(()->new ResourceNotFoundException("Guardian Notice", "guardian notice id", id));
		guardianNotice.setNoticeTitle(guardianNoticeDto.getNoticeTitle());
		guardianNotice.setNoticeDesc(guardianNoticeDto.getNoticeDesc());
		guardianNotice.setCreationDate(guardianNoticeDto.getCreationDate());
		guardianNotice.setStatus(guardianNoticeDto.getStatus());
		GuardianNotice updatedNotice = guardianNoticeRepos.save(guardianNotice);
		GuardianNoticeDto guardianNoticeToDto = GuardianNoticeToDto(updatedNotice);	
		return guardianNoticeToDto;
	}

	@Override
	public void deleteGuardianNotice(long id) {
		GuardianNotice guardianNotice = guardianNoticeRepos.findById(id).orElseThrow(()->new ResourceNotFoundException("Guardian Notice", "guardian notice id", id));
		guardianNoticeRepos.delete(guardianNotice);

	}

	@Override
	public GuardianNoticeDto getGuardianNoticeById(long id) {
		GuardianNotice guardianNotice = guardianNoticeRepos.findById(id).orElseThrow(()->new ResourceNotFoundException("Guardian Notice", "guardian notice id", id));
		GuardianNoticeDto guardianNoticeDto = GuardianNoticeToDto(guardianNotice);
		return guardianNoticeDto;
	}

	@Override
	public List<GuardianNoticeDto> getAllGuardianNotices() {
		List<GuardianNotice> all = guardianNoticeRepos.findAll();
		List<GuardianNoticeDto> list = all.stream().map((lst)->GuardianNoticeToDto(lst)).collect(Collectors.toList());
		return list;
	}
	
	public GuardianNoticeDto GuardianNoticeToDto(GuardianNotice guardianNotice) {
		GuardianNoticeDto guardianNoticeDto = modelMapper.map(guardianNotice, GuardianNoticeDto.class);
		return guardianNoticeDto;
	}
	
	public GuardianNotice dtoToGuardianNotice(GuardianNoticeDto guardianNoticeDto) {
		GuardianNotice guardianNotice = modelMapper.map(guardianNoticeDto, GuardianNotice.class);
		return guardianNotice;
	}

}
