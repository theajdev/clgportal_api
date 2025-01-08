package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.dto.StudentDto;

public interface StudentService {
	public StudentDto newStudent(StudentDto studDto);
	public StudentDto updateStudent(StudentDto studDto,long id);
	public void deleteStudent(long id);
	public StudentDto getStudentById(long id);
	public List<StudentDto> getAllStudents();
}
