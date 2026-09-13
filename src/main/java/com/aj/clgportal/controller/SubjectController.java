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
import com.aj.clgportal.dto.DeleteRequestDto;
import com.aj.clgportal.dto.DeleteSelectedIdsResponseDto;
import com.aj.clgportal.dto.SubjectDto;
import com.aj.clgportal.service.SubjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subject")
public class SubjectController {

	@Autowired
	SubjectService subjectServ;

	@PostMapping("/")
	public ResponseEntity<SubjectDto> addSubject(@Valid @RequestBody SubjectDto subjectDto){
		SubjectDto newSubject = subjectServ.newSubject(subjectDto);
		return new ResponseEntity<SubjectDto>(newSubject,HttpStatus.CREATED);
	}
	
	@PutMapping("/{subjectId}")
	public ResponseEntity<SubjectDto> updatedSubject(@PathVariable Long subjectId, @RequestBody SubjectDto subjectDto){
		SubjectDto editSubject = subjectServ.editSubject(subjectId, subjectDto);
		return ResponseEntity.ok(editSubject);
	}
	
	@DeleteMapping("/{subjectId}")
	public ResponseEntity<ApiResponse> deleteSubject(@PathVariable Long subjectId){
		subjectServ.removeSubject(subjectId);
		return new ResponseEntity<>(new ApiResponse("Subject Deleted.",true),HttpStatus.OK);
	}
	
	@GetMapping("/{subjectId}")
	public ResponseEntity<SubjectDto> getSubject(@PathVariable Long subjectId){
		SubjectDto viewSubject = subjectServ.viewSubject(subjectId);
		return new ResponseEntity<>(viewSubject,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<SubjectDto>> getAllSubjects(){
		List<SubjectDto> subjects = subjectServ.viewAllSubjects();
		return ResponseEntity.ok(subjects);
	}
	
	@GetMapping("/status/{status}")
	public ResponseEntity<List<SubjectDto>> SubjectByStatus(@PathVariable Character status){
		List<SubjectDto> subjectByStatus = subjectServ.getSubjectByStatus(status);
		return ResponseEntity.ok(subjectByStatus);
		
	}
	
	@PostMapping("/delete-selected")
	public ResponseEntity<List<DeleteSelectedIdsResponseDto>> deleteSelectedSubjectIds( @RequestBody DeleteRequestDto request){
		
		 return ResponseEntity.ok(subjectServ.deleteSelectedSubjects(
		            request.getModule(),
		            request.getIds()
		    ));
	}
}
