package com.aj.clgportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aj.clgportal.util.AppConstants;
import com.aj.clgportal.entity.Notification;
import com.aj.clgportal.entity.NotificationUser;
import com.aj.clgportal.service.NotificationService;
import com.aj.clgportal.service.NotificationUserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

	@Autowired
	private NotificationUserService notificationService;

	@Autowired
	private NotificationService notificationServ;

	@GetMapping("/notifications/unread/{username}")
	public List<NotificationUser> getUnread(@PathVariable String username) {
		return notificationService.getUnreadForCurrentUser(username);
	}

	@GetMapping("notifications/{deptId}")
	public List<Notification> getAllNotificationsByDept(@PathVariable Long deptId,
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@RequestParam(defaultValue = "") String search) {
		return notificationServ.getAllNotificationsByDept(deptId, pageSize, pageNumber, sortBy, sortDir, search);
	}

	@GetMapping("/notification/{notificationId}")
	public List<Notification> getNotificationDetails(@PathVariable Long notificationId) {
		return notificationServ.getNotificationDetails(notificationId);
	}

	@PutMapping("/notifications/mark-all-read/{username}")
	public ResponseEntity<?> markAll(@PathVariable String username) {
		notificationService.markAllAsRead(username);
		return ResponseEntity.ok("All marked as read");
	}

	@PutMapping("/notifications/mark-one/{notificationId}/{username}")
	public ResponseEntity<?> markOne(@PathVariable Long notificationId, @PathVariable String username) {
		notificationService.markOneAsRead(notificationId, username);
		return ResponseEntity.ok("Marked one as read");
	}
}
