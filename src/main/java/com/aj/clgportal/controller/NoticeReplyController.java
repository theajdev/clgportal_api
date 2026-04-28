package com.aj.clgportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aj.clgportal.dto.NoticeReplyDto;
import com.aj.clgportal.entity.NoticeReply;
import com.aj.clgportal.service.NoticeReplyService;

@RestController
@RequestMapping("/api/notice")
public class NoticeReplyController {

	@Autowired
	private NoticeReplyService noticeReplyService;

	@PostMapping("/{id}/reply")
	public ResponseEntity<?> replyToNotice(@PathVariable Long id, @RequestBody NoticeReply reply,
			Authentication authentication) {

		Long deptId = noticeReplyService.getUserDeptId(authentication);

		reply.setUsername(authentication.getName()); // sender

		Long noticeId = noticeReplyService.getNoticeId(id);

		noticeReplyService.addReply(noticeId, reply, deptId, authentication);

		return ResponseEntity.ok("Reply added");
	}

	@GetMapping("/{id}/replies")
	public ResponseEntity<?> getReplies(@PathVariable Long id) {
		List<NoticeReplyDto> repliesByNotice = noticeReplyService.getRepliesByNotice(id);
		return ResponseEntity.ok(repliesByNotice);
	}
}