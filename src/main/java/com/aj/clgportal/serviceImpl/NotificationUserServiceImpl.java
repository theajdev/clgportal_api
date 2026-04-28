package com.aj.clgportal.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aj.clgportal.entity.NotificationUser;
import com.aj.clgportal.repository.NotificationUserRepository;
import com.aj.clgportal.service.NotificationUserService;

@Service
public class NotificationUserServiceImpl implements NotificationUserService {

	@Autowired
	NotificationUserRepository notificationUserRepo;

	@Override
	public List<NotificationUser> getUnreadForCurrentUser(String username) {
		return notificationUserRepo.findByUsernameAndReadFalse(username);
	}

	@Override
	public void markOneAsRead(Long notificationId, String username) {

		NotificationUser nu = notificationUserRepo.findByNotificationIdAndUsername(notificationId, username)
				.orElseThrow();

		nu.setRead(true);
		notificationUserRepo.save(nu);

	}

	@Override
	public void markAllAsRead(String username) {

		List<NotificationUser> list = notificationUserRepo.findByUsernameAndReadFalse(username);

		list.forEach(n -> n.setRead(true));

		notificationUserRepo.saveAll(list);
	}
}
