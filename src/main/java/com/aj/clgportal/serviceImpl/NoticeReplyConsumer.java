package com.aj.clgportal.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.NoticeReplyEvent;

@Service
public class NoticeReplyConsumer {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@KafkaListener(topics = "notice-reply-topic", groupId = "notice-reply-group")
	public void consumeReply(NoticeReplyEvent event) {

		// 🔥 Send to specific notice subscribers
		messagingTemplate.convertAndSend("/topic/notice/" + event.getNoticeId(), event);
	}
	
	
}
