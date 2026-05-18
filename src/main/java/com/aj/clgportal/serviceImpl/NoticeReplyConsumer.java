package com.aj.clgportal.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.NoticeReplyEvent;
import com.aj.clgportal.entity.Notification;
import com.aj.clgportal.entity.NotificationUser;
import com.aj.clgportal.repository.NotificationRepository;
import com.aj.clgportal.repository.NotificationUserRepository;
import com.aj.clgportal.repository.StudentRepository;
import com.aj.clgportal.repository.TeacherRepository;

@Service
public class NoticeReplyConsumer {

	private final NotificationRepository notificationRepository;
	private final NotificationUserRepository notificationUserRepository;
	private final TeacherRepository teacherRepository;
	private final StudentRepository studentRepository;
	private final SimpMessagingTemplate messagingTemplate;
	
	public NoticeReplyConsumer(NotificationRepository notificationRepository,
			NotificationUserRepository notificationUserRepository, TeacherRepository teacherRepository,
			StudentRepository studentRepository, SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
		this.notificationRepository = notificationRepository;
		this.notificationUserRepository = notificationUserRepository;
		this.studentRepository = studentRepository;
		this.teacherRepository = teacherRepository;
	}
	
	
	
	@KafkaListener(topics = "notice-reply-topic", groupId = "notice-reply-group")
	public void consumeReply(NoticeReplyEvent event) {
		
	    String loggedUser = event.getUsername(); // ✅ THIS is correct

		if (event.getDeptIds() == null || event.getDeptIds().isEmpty()) {
			System.out.println("DeptIds is null/empty. Skipping event.");
			return;
		}

		// ✅ Step 1: collect ALL users ONCE
		Set<String> allUsers = new HashSet<>();

		for (Long deptId : event.getDeptIds()) {

			allUsers.addAll(teacherRepository.findByDepts_Id(deptId).stream().map(t -> t.getUsername()).toList());

			allUsers.addAll(studentRepository.findByDepts_Id(deptId).stream().map(s -> s.getUsername()).toList());
		}

		// ✅ Step 2: loop per department notification
		for (Long deptId : event.getDeptIds()) {

			Optional<Notification> notificationOpt = notificationRepository
					.findByNoticeIdAndDepartmentId(event.getNoticeId(), deptId);

			if (notificationOpt.isEmpty()) {
				System.out.println("Notification not found for noticeId: " + event.getNoticeId());
				continue;
			}

			Notification notification = notificationOpt.get();

			// ✅ STEP 3: Fetch existing users in ONE query
			List<NotificationUser> existingList = notificationUserRepository
					.findByNotificationIdAndUsernameIn(notification.getId(), allUsers);

			Map<String, NotificationUser> existingMap = existingList.stream()
					.collect(Collectors.toMap(NotificationUser::getUsername, nu -> nu));

			List<NotificationUser> toInsert = new ArrayList<>();
			List<NotificationUser> toUpdate = new ArrayList<>();

			// ✅ STEP 4: Split insert/update
			for (String username : allUsers) {
				
				if (existingMap.containsKey(username)) {
					NotificationUser existing = existingMap.get(username);
					if(username.equalsIgnoreCase(loggedUser)) {
						existing.setRead(true);	
					}else{
						existing.setRead(false);
					}
					toUpdate.add(existing);

				} else {
					NotificationUser nu = new NotificationUser();
					nu.setNotificationId(notification.getId());
					nu.setUsername(username);
					nu.setRead(false);
					toInsert.add(nu);
				}
			}

			// ✅ STEP 5: Batch DB operations
			if (!toUpdate.isEmpty()) {
				notificationUserRepository.saveAll(toUpdate);
			}

			if (!toInsert.isEmpty()) {
				notificationUserRepository.saveAll(toInsert);
			}

			// ✅ STEP 6: Send WebSocket event
			for (String username : allUsers) {

				// Optional: skip sender
				// if (username.equals(event.getUsername())) continue;

				Map<String, Object> payload = new HashMap<>();
				payload.put("type", "REPLY");
				payload.put("notificationId", notification.getId());
				payload.put("noticeId", event.getNoticeId());
				payload.put("username", event.getRepliedBy());

				messagingTemplate.convertAndSend("/topic/notifications/" + username, payload);
			}

		}
	}

}
