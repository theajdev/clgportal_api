package com.aj.clgportal.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.NoticeReplyEvent;

@Service
public class NoticeReplyProducer {
	@Autowired
	private KafkaTemplate<String, NoticeReplyEvent> kafkaTemplate;
	
	public void sendNoticeReply(NoticeReplyEvent event) {
		kafkaTemplate.send("notice-reply-topic", event);
	}
}
