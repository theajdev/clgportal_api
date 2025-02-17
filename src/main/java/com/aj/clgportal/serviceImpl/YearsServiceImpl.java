package com.aj.clgportal.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.YearsDto;
import com.aj.clgportal.entity.Years;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.YearsRepository;
import com.aj.clgportal.service.YearsService;

@Service
public class YearsServiceImpl implements YearsService {
	
	
	@Autowired
	YearsRepository yearsRepository;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public YearsDto newYear(YearsDto yearsDto) {
		Years years = dtoToYears(yearsDto);
		Years newYears = yearsRepository.save(years);
		YearsDto newYearsDto = yearsToDto(newYears);
		return newYearsDto;
	}

	@Override
	public YearsDto updateYear(YearsDto yearsDto, long id) {
		Years year = yearsRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Year", "year id", id));
		year.setYearDesc(yearsDto.getYearDesc());
		year.setStatus(yearsDto.getStatus());
		return null;
	}

	@Override
	public void deleteYear(long id) {
		Years year = yearsRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Year", "year id", id));
		yearsRepository.delete(year);

	}

	@Override
	public YearsDto getYearById(long id) {
		Years year = yearsRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Year", "year id", id));
		YearsDto yearsDto = yearsToDto(year);
		return yearsDto;
	}

	@Override
	public List<YearsDto> getAllYears() {
		List<Years> allYears = yearsRepository.findAll();
		List<YearsDto> list = allYears.stream().map((lst)->yearsToDto(lst)).collect(Collectors.toList());
		return list;
	}
	
	public YearsDto yearsToDto(Years years) {
		YearsDto dto = modelMapper.map(years, YearsDto.class);
		return dto;
	}
	
	public Years dtoToYears(YearsDto yearsDto) {
		Years years = modelMapper.map(yearsDto, Years.class);
		return years;
	}
		

}
