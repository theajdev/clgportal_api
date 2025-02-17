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
import com.aj.clgportal.dto.GuardianNoticeDto;
import com.aj.clgportal.service.GuardianNoticeService;

@RestController
@RequestMapping("/api/guardianNotice")
public class GuardianNoticeController {
	
	@Autowired
	GuardianNoticeService guardianNoticeService;
	
	@PostMapping("/")
	public ResponseEntity<GuardianNoticeDto> newGuardianNotice(@RequestBody GuardianNoticeDto guardianNoticeDto){
		GuardianNoticeDto newGuardianNotice = guardianNoticeService.newGuardianNotice(guardianNoticeDto);
		return new ResponseEntity<GuardianNoticeDto>(newGuardianNotice,HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<GuardianNoticeDto> updateGuardianNotice(@RequestBody GuardianNoticeDto guardianNoticeDto,@PathVariable long id){
		GuardianNoticeDto updateGuardianNotice = guardianNoticeService.updateGuardianNotice(guardianNoticeDto, id);
		return ResponseEntity.ok(updateGuardianNotice);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteGaurdianNotice(@PathVariable long id){
		guardianNoticeService.deleteGuardianNotice(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Guardin notice deleted.", true),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<GuardianNoticeDto> getGuardianNoticeById(@PathVariable long id){
		GuardianNoticeDto guardianNoticeById = guardianNoticeService.getGuardianNoticeById(id);
		return new ResponseEntity<GuardianNoticeDto>(guardianNoticeById,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<GuardianNoticeDto>> getAllGuardianNotices(){
		List<GuardianNoticeDto> allGuardianNotices = guardianNoticeService.getAllGuardianNotices();
		return ResponseEntity.ok(allGuardianNotices);
	}
}
