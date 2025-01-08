package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.dto.TeacherDto;

public interface TeacherService {
	public TeacherDto newTeacher(TeacherDto teacherDto);
	public TeacherDto updateTeacher(TeacherDto teacherDto,long id);
	public void deleteTeacher(long id);
	public TeacherDto getTeacherById(long id);
	public List<TeacherDto> getAllTeachers();
	
}
