package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.entity.NotificationUser;

public interface NotificationUserService {
	public List<NotificationUser> getUnreadForCurrentUser(String username,Long deptId);
	public void markOneAsRead(Long notificationId,String username);
	public void markAllAsRead(String username, Long deptId);
}
