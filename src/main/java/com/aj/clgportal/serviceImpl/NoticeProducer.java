package com.aj.clgportal.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.NoticeEvent;

@Service
public class NoticeProducer {
	@Autowired
	private KafkaTemplate<String, NoticeEvent> kafkaTemplate;

	public void sendNotice(NoticeEvent event) {
		kafkaTemplate.send("notice-topic", event);
	}
}
