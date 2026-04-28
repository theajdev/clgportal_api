package com.aj.clgportal.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.aj.clgportal.dto.NoticeDto;
import com.aj.clgportal.dto.NoticeReadDto;

public interface NoticeService {
	public NoticeDto newNotice(NoticeDto dto,List<MultipartFile> files);
	public NoticeDto updateNotice(NoticeDto dto, Long id,List<MultipartFile> files);
	public void removeNotice(Long id);
	public void deleteNoticeReadDetails(Long noticeId);
	public NoticeDto getNoticeById(Long id);
	public List<NoticeDto> getAllNotices();
	public Long getMaxNoticeId();
	public void resetNoticeSequence(Long id);
	public List<NoticeDto> getNoticeByStatus(Character status);
	public void removeDepartmentNotice(Long id);
	public Long getCountOfNotice(Character status); 
	public List<NoticeDto> getNoticesByDepts(Long deptId);
	public void markNoticeAsRead(Long noticeId, Long userId, String userType);
	public List<NoticeReadDto> getNoticeReadDetails(Long noticeId);
}
