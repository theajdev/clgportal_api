package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.dto.GuardianNoticeDto;

public interface GuardianNoticeService {
	public GuardianNoticeDto newGuardianNotice(GuardianNoticeDto guardianNoticeDto);
	public GuardianNoticeDto updateGuardianNotice(GuardianNoticeDto guardianNoticeDto,long id);
	public void deleteGuardianNotice(long id);
	public GuardianNoticeDto getGuardianNoticeById(long id);
	public List<GuardianNoticeDto> getAllGuardianNotices();
}
