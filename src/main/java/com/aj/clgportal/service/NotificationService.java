package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.entity.Notification;

public interface NotificationService {
	public List<Notification> getNotificationDetails(Long notificationId);
	public List<Notification> getAllNotificationsByDept(Long deptId, int pageSize, int pageNumber, String sortBy, String sortDir, String search);
	/*
	 * void markAllAsRead(Long deptId); public void markOneAsRead(Long id);
	 */
}
