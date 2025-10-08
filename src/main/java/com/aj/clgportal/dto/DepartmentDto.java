package com.aj.clgportal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentDto {
	private long id;
	private String deptDesc;
	private Character status;
	private StudentDto student;
	private TeacherDto teacher;
}
