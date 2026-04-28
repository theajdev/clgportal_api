package com.aj.clgportal.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.aj.clgportal.dto.NoticeReplyDto;
import com.aj.clgportal.entity.NoticeReply;

public interface NoticeReplyService {
	public Long getNoticeId(Long noticeId);

	public void addReply(Long noticeId, NoticeReply reply, Long userDeptId, Authentication authentication);

	public Long getUserDeptId(Authentication authentication);

	public List<NoticeReplyDto> getRepliesByNotice(Long noticeId);
}
