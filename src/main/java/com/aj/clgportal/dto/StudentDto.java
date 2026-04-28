package com.aj.clgportal.dto;

import java.util.Date;

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
	public long mobileNo;
	public String address;
	private String username;
	private String email;
	private String password;
	private String guardianName;
	private String profilePic;
	private Character status;
	private String about;
	private Date postedOn;
	private Date updatedOn;
	private TeacherDto teacher;
	private long deptId;
}
