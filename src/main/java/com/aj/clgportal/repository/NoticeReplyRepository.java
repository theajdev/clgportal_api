package com.aj.clgportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aj.clgportal.entity.NoticeReply;

public interface NoticeReplyRepository extends JpaRepository<NoticeReply, Long> {
	public List<NoticeReply> findByNoticeIdOrderByRepliedOnAsc(Long noticeId);
}
