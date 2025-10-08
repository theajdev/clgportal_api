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
import com.aj.clgportal.dto.TeacherDto;
import com.aj.clgportal.service.TeacherService;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
	
	@Autowired
	TeacherService teacherServ;
	
	@PostMapping("/")
	public ResponseEntity<TeacherDto> newTeacher(@RequestBody TeacherDto teacherDto){
		TeacherDto teacher = teacherServ.newTeacher(teacherDto);
		return new ResponseEntity<TeacherDto>(teacher,HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<TeacherDto> updateTeacher(@RequestBody TeacherDto teacherDto, @PathVariable long id){
		TeacherDto updatedTeacher = teacherServ.updateTeacher(teacherDto, id);
		return ResponseEntity.ok(updatedTeacher);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteTeacher(@PathVariable long id){
		teacherServ.removeTeacherRole(id);
		teacherServ.deleteTeacher(id);
		Long maxTeacherId = teacherServ.getMaxTeacherId();
		teacherServ.resetTeacherSequence(maxTeacherId+1);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Teacher deleted successfully.", true),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TeacherDto> getTeacherById(@PathVariable long id){
		TeacherDto teacher = teacherServ.getTeacherById(id);
		return new ResponseEntity<TeacherDto>(teacher,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<TeacherDto>> getAllTeachers(){
		List<TeacherDto> allTeachers = teacherServ.getAllTeachers();
		return ResponseEntity.ok(allTeachers);
	}
	
	@GetMapping("/status/{status}")
	public ResponseEntity<List<TeacherDto>> getTeacherByStatus(@PathVariable Character status){
		List<TeacherDto> teachers = teacherServ.getTeacherByStatus(status);
		return ResponseEntity.ok(teachers);
	}
	
	@GetMapping("/count")
	public ResponseEntity<String> getTeacherCount(){
		Long teacherCount = teacherServ.getTeacherCount('V');
		return ResponseEntity.ok(String.format("%02d", teacherCount));
	}
}
