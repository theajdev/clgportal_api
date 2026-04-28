package com.aj.clgportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aj.clgportal.entity.NoticeAttachment;

public interface NoticeAttachmentRepository extends JpaRepository<NoticeAttachment, Long> {
	public List<NoticeAttachment> findByNoticeId(Long Id);
}
