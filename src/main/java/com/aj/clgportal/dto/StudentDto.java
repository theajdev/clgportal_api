package com.aj.clgportal.dto;

import com.aj.clgportal.entity.Teacher;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentDto {
	private long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String userName;
	private String email;
	private String password;
	private String guardianName;
	private String profilePic;
	private Character status;
	private Teacher teacher;
}
