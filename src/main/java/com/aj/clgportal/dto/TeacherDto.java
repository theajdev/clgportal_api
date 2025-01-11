package com.aj.clgportal.dto;

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
	public String username;
	public String email;
	public String password;
	public String profilePic;
	public Character status;
	private long deptId;
}
