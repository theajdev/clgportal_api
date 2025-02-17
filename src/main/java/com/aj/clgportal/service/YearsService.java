package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.dto.YearsDto;

public interface YearsService {
	public YearsDto newYear(YearsDto yearsDto);
	public YearsDto updateYear(YearsDto yearsDto,long id);
	public void deleteYear(long id);
	public YearsDto getYearById(long id);
	public List<YearsDto> getAllYears();
}
