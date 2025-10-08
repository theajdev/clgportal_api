package com.aj.clgportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aj.clgportal.dto.ApiResponse;
import com.aj.clgportal.dto.NoticeDto;
import com.aj.clgportal.service.NoticeService;


@RestController
@RequestMapping("/api/notice")
public class NoticeController {

	@Autowired
	private NoticeService noticeServ;


	@PostMapping("/")
	public ResponseEntity<NoticeDto> newNotice(@RequestBody NoticeDto dto){
		System.out.println("dto: "+dto.getDeptId());
		NoticeDto newNotice = noticeServ.newNotice(dto);
		return new ResponseEntity<>(newNotice,HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<NoticeDto> updateNotice(@RequestBody NoticeDto dto, @PathVariable Long id){
		NoticeDto updatedNotice = noticeServ.updateNotice(dto, id);
		return ResponseEntity.ok(updatedNotice);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteNotice(@PathVariable Long id){
		noticeServ.removeDepartmentNotice(id);
		noticeServ.removeNotice(id);
		Long maxNoticeId = noticeServ.getMaxNoticeId();
		noticeServ.resetNoticeSequence(maxNoticeId+1);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Notice deleted.", true),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<NoticeDto> getNoticeById(@PathVariable Long id){
		NoticeDto notice = noticeServ.getNoticeById(id);
		return new ResponseEntity<>(notice,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<NoticeDto>> getAllNotices(){
		List<NoticeDto> notices = noticeServ.getAllNotices();
		return ResponseEntity.ok(notices);
	}
	
	@GetMapping("/status/{status}")
	public ResponseEntity<List<NoticeDto>> getAllNotices(@PathVariable Character status){
		List<NoticeDto> notices = noticeServ.getNoticeByStatus(status);
		return ResponseEntity.ok(notices);
	}
	
	@GetMapping("/count")
	public ResponseEntity<String> getNoticeCount(){
		Long countOfNotice = noticeServ.getCountOfNotice('V');
		return ResponseEntity.ok(String.format("%02d", countOfNotice));
	}
}
