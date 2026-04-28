package com.aj.clgportal.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.aj.clgportal.controller.AdminController;
import com.aj.clgportal.dto.AttachmentEventDto;
import com.aj.clgportal.dto.NoticeEvent;
import com.aj.clgportal.entity.Notification;
import com.aj.clgportal.entity.NotificationUser;
import com.aj.clgportal.repository.NotificationRepository;
import com.aj.clgportal.repository.NotificationUserRepository;
import com.aj.clgportal.repository.TeacherRepository;
import com.aj.clgportal.util.NoticeType;
import com.aj.clgportal.repository.StudentRepository;

@Service
public class NoticeConsumer {

	//private final AdminController adminController;
	private final NotificationRepository notificationRepository;
	private final NotificationUserRepository notificationUserRepository;
	private final TeacherRepository teacherRepository;
	private final StudentRepository studentRepository;
	private final SimpMessagingTemplate messagingTemplate;

	public NoticeConsumer(NotificationRepository notificationRepository,
			NotificationUserRepository notificationUserRepository, TeacherRepository teacherRepository,
			StudentRepository studentRepository, SimpMessagingTemplate messagingTemplate,
			AdminController adminController) {

		this.notificationRepository = notificationRepository;
		this.notificationUserRepository = notificationUserRepository;
		this.teacherRepository = teacherRepository;
		this.studentRepository = studentRepository;
		this.messagingTemplate = messagingTemplate;
		//this.adminController = adminController;
	}

	@KafkaListener(topics = "notice-topic", groupId = "clg-group")
	public void consume(NoticeEvent event) {
		
		for (Long deptId : event.getDeptIds()) {

	        // ⚠️ Handle multiple attachments
	        if (event.getAttachments() != null && !event.getAttachments().isEmpty()) {

	            for (AttachmentEventDto att : event.getAttachments()) {

	                // ✅ UPDATE
	                Notification notification = new Notification();

	                if (event.getType() == NoticeType.UPDATE) {
	                	notification = notificationRepository
		                        .findByTitleAndDepartmentId(event.getTitle(), deptId)
		                        .orElse(new Notification());
	                    notification.setUpdatedAt(LocalDateTime.now());
	                } else {
	                    notification.setCreatedAt(LocalDateTime.now());
	                }

	                // 🔥 COMMON FIELDS
	                notification.setTitle(event.getTitle());
	                notification.setMessage(event.getDescription());
	                notification.setSentBy(event.getSentBy());
	                notification.setDepartmentId(deptId);
	                notification.setSendersUsername(event.getSendersUsername());
	                notification.setSendersProfilePic(event.getSendersProfilePic());

	                // 🔥 ATTACHMENT FIELDS
	                notification.setFileName(att.getFileName());
	                notification.setContentType(att.getContentType());
	                notification.setSize(att.getSize());
	                notification.setFileUrl(att.getFileUrl());
	                notification.setNoticeId(att.getNoticeId());
	                notification = notificationRepository.save(notification);

	                handleUsers(notification, deptId);
	            }

	        } else {

	        	// ✅ UPDATE
                Notification notification = new Notification();

                if (event.getType() == NoticeType.UPDATE) {
                	notification = notificationRepository
	                        .findByTitleAndDepartmentId(event.getTitle(), deptId)
	                        .orElse(new Notification());
                    notification.setUpdatedAt(LocalDateTime.now());
                } else {
                    notification.setCreatedAt(LocalDateTime.now());
                }

	            notification.setTitle(event.getTitle());
	            notification.setMessage(event.getDescription());
	            notification.setSentBy(event.getSentBy());
	            notification.setDepartmentId(deptId);
	            notification.setSendersUsername(event.getSendersUsername());
	            notification.setSendersProfilePic(event.getSendersProfilePic());
	            notification.setNoticeId(event.getNoticeId());
	            notification = notificationRepository.save(notification);

	            handleUsers(notification, deptId);
	        }
	    }

	}
	
	private void handleUsers(Notification notification, Long deptId) {

	    Set<String> allUsers = new HashSet<>();

	    allUsers.addAll(
	        teacherRepository.findByDepts_Id(deptId)
	            .stream().map(t -> t.getUsername()).toList()
	    );

	    allUsers.addAll(
	        studentRepository.findByDepts_Id(deptId)
	            .stream().map(s -> s.getUsername()).toList()
	    );

	    for (String username : allUsers) {

	        Optional<NotificationUser> existingOpt =
	            notificationUserRepository.findByNotificationIdAndUsername(
	                notification.getId(), username
	            );

	        if (existingOpt.isPresent()) {
	            NotificationUser existing = existingOpt.get();
	            existing.setRead(false);
	            notificationUserRepository.save(existing);
	        } else {
	            NotificationUser nu = new NotificationUser();
	            nu.setNotificationId(notification.getId());
	            nu.setUsername(username);
	            nu.setRead(false);
	            notificationUserRepository.save(nu);
	        }

	        // 🔥 WebSocket push
	        messagingTemplate.convertAndSend(
	            "/topic/notifications/" + username,
	            notification
	        );
	    }
	}
}