package com.aj.clgportal.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aj.clgportal.dto.ApiResponse;
import com.aj.clgportal.dto.StudentDto;
import com.aj.clgportal.service.ProfilePicService;
import com.aj.clgportal.service.StudentService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/student")
public class StudentController {

	@Autowired
	StudentService studentService;

	@Autowired
	private ProfilePicService picService;

	@Value("${profile.pic}")
	private String path;

	@PostMapping("/")
	public ResponseEntity<StudentDto> newStudent(@RequestBody StudentDto studentDto, HttpSession session) {
		System.out.println("password: " + studentDto.getPassword());
		StudentDto newStudent = studentService.newStudent(studentDto, session);
		return new ResponseEntity<StudentDto>(newStudent, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StudentDto> updateStudent(@RequestBody StudentDto studentDto, @PathVariable long id) {
		StudentDto updatedStudent = studentService.updateStudent(studentDto, id);
		return ResponseEntity.ok(updatedStudent);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteStudent(@PathVariable long id) {
		studentService.removeStudentRole(id);
		studentService.deleteStudent(id);
		Long maxTeacherId = studentService.getMaxStudentId();
		studentService.resetStudentSequence(maxTeacherId + 1);

		return new ResponseEntity<ApiResponse>(new ApiResponse("Student deleted successfully.", true), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StudentDto> getStudentById(@PathVariable long id) {
		StudentDto student = studentService.getStudentById(id);
		return new ResponseEntity<StudentDto>(student, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<StudentDto>> getAllStudents() {
		List<StudentDto> allStudents = studentService.getAllStudents();
		return ResponseEntity.ok(allStudents);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<StudentDto>> getTeacherByStatus(@PathVariable Character status) {
		List<StudentDto> students = studentService.getStudentByStatus(status);
		return ResponseEntity.ok(students);
	}

	// Student Profile pic upload
	@PostMapping("/profile/upload/{studentId}")
	public ResponseEntity<StudentDto> uploadPostImage(@PathVariable Long studentId, @RequestParam MultipartFile image)
			throws IOException {
		String fileName = picService.uploadProfilePic(path, image);
		StudentDto student = studentService.getStudentById(studentId);
		student.setProfilePic(fileName);
		StudentDto updateStudent = studentService.updateStudent(student, studentId);
		return new ResponseEntity<StudentDto>(updateStudent, HttpStatus.OK);
	}

	// Download image
	@GetMapping(value = "/profile/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = picService.getResource(path, imageName);
		response.setContentType(org.springframework.http.MediaType.IMAGE_JPEG_VALUE);
		org.springframework.util.StreamUtils.copy(resource, response.getOutputStream());
	}
}
