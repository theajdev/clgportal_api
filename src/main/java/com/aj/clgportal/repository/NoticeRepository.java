package com.aj.clgportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aj.clgportal.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	boolean existsByNoticeTitle(String noticeTitle);
	@Query(value = "select COALESCE(max(n.notice_id),0) from tbl_notice n",nativeQuery = true)
	Long findMaxNoticeId();
	
	List<Notice> findByStatus(Character status);
	
	long countByStatus(Character status);
	
	@Query(value="SELECT n.notice_id,n.notice_title,n.notice_desc,n.status,n.posted_on,n.updated_on FROM tbl_notice n JOIN tbl_notice_department nd ON n.notice_id=nd.notice_id WHERE nd.dept_id = :deptId",nativeQuery = true)
	List<Notice> findByDeptsNotices(@Param("deptId") Long deptId);

}
