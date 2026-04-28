package com.aj.clgportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aj.clgportal.entity.NotificationUser;

public interface NotificationUserRepository extends JpaRepository<NotificationUser, Long> {
	List<NotificationUser> findByUsernameAndReadFalse(String username);

	Optional<NotificationUser> findByNotificationIdAndUsername(Long notificationId, String username);

}
