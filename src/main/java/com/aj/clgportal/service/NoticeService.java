package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.dto.NoticeDto;

public interface NoticeService {
	public NoticeDto newNotice(NoticeDto dto);
	public NoticeDto updateNotice(NoticeDto dto, Long id);
	public void removeNotice(Long id);
	public NoticeDto getNoticeById(Long id);
	public List<NoticeDto> getAllNotices();
	public Long getMaxNoticeId();
	public void resetNoticeSequence(Long id);
	public List<NoticeDto> getNoticeByStatus(Character status);
	public void removeDepartmentNotice(Long id);
	public Long getCountOfNotice(Character status); 
}
