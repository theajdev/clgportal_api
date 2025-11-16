package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.dto.StudentDto;

import jakarta.servlet.http.HttpSession;

public interface StudentService {
	public StudentDto newStudent(StudentDto studDto,HttpSession session);
	public StudentDto updateStudent(StudentDto studDto,long id,HttpSession session);
	public void deleteStudent(long id);
	public StudentDto getStudentById(long id);
	public List<StudentDto> getAllStudents();
	public void removeStudentRole(Long id);
	public Long getMaxStudentId();
	public void resetStudentSequence(Long nextVal);
	public List<StudentDto> getStudentByStatus(Character status);
}
