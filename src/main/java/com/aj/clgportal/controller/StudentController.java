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
import com.aj.clgportal.dto.StudentDto;
import com.aj.clgportal.service.StudentService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@PostMapping("/")
	public ResponseEntity<StudentDto> newStudent(@RequestBody StudentDto studentDto,HttpSession session) {
		StudentDto newStudent = studentService.newStudent(studentDto,session);
		return new ResponseEntity<StudentDto>(newStudent,HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<StudentDto> updateStudent(@RequestBody StudentDto studentDto, @PathVariable long id,HttpSession session){
		StudentDto updatedStudent = studentService.updateStudent(studentDto, id,session);
		return ResponseEntity.ok(updatedStudent);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteStudent(@PathVariable long id){
		studentService.deleteStudent(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Student deleted successfully.", true),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<StudentDto> getStudentById(@PathVariable long id){
		StudentDto student = studentService.getStudentById(id);
		return new ResponseEntity<StudentDto>(student,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StudentDto>> getAllStudents(){
		List<StudentDto> allStudents = studentService.getAllStudents();
		return ResponseEntity.ok(allStudents);
	}
}
