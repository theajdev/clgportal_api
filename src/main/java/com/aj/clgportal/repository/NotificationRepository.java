package com.aj.clgportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aj.clgportal.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findNotificationById(Long notificationId);

	Page<Notification> findByDepartmentIdAndTitleContainingIgnoreCase(Long deptId, String message, Pageable pageable);

	Optional<Notification> findByTitleAndDepartmentId(String title, Long deptId);

	Optional<Notification> findByNoticeIdAndDepartmentId(Long noticeId, Long deptId);

	Notification findByNoticeId(Long noticeId);

	@Query(value = """
						select nt.id,nt.content_type,nt.created_at,nt.department_id,nt.file_name,nt.file_url,nt.message,nt.notice_id,nt.read,nt.senders_profile_pic,nt.senders_username,nt.sent_by,nt.size,nt.title,nt.update_at
			from notification_user nu JOIN
			notification nt ON nt.id=nu.notification_id
			and nt.department_id= :deptId and nu.username=:username and not(nu.read)
						""", nativeQuery = true)
	List<Notification> getReplies(@Param("username") String username, @Param("deptId") Long deptId);

}
