package com.aj.clgportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aj.clgportal.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findNotificationById(Long notificationId);
	
	Page<Notification> findByDepartmentIdAndTitleContainingIgnoreCase(
		    Long deptId, String message, Pageable pageable
		);
	
	Optional<Notification> findByTitleAndDepartmentId(String title, Long deptId);
}
