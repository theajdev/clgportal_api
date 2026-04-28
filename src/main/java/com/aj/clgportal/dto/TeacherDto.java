package com.aj.clgportal.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeacherDto {
	public long id;
	public String firstName;
	public String middleName;
	public String lastName;
	public long mobileNo;
	public String address;
	public String username;
	public String email;
	public String password;
	public String profilePic;
	public Character status;
	private String designation;
	private String about;
	private Date postedOn;
	private Date updatedOn;
	private long deptId;

}
