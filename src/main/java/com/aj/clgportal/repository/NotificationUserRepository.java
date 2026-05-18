package com.aj.clgportal.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aj.clgportal.entity.NotificationUser;

public interface NotificationUserRepository extends JpaRepository<NotificationUser, Long> {
	@Query(value = """
						select nu1_0.id,nu1_0.notification_id,nu1_0.read,nu1_0.username
			from notification_user nu1_0 JOIN
			notification nt ON nt.id=nu1_0.notification_id
			and nt.department_id= :deptId and nu1_0.username= :username
						""", nativeQuery = true)
	List<NotificationUser> findByUsernameAndReadFalse(@Param("username") String username, @Param("deptId") Long deptId);

	Optional<NotificationUser> findByNotificationIdAndUsername(Long notificationId, String username);

	List<NotificationUser> findByNotificationIdAndUsernameIn(Long notificationId, Set<String> usernames);
	
	
	
}
