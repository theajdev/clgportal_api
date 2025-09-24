package com.aj.clgportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aj.clgportal.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	boolean existsByNoticeTitle(String noticeTitle);
	@Query(value = "select COALESCE(max(n.notice_id),0) from tbl_notice n",nativeQuery = true)
	Long findMaxNoticeId();
	
	List<Notice> findByStatus(Character status);
}
