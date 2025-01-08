package com.aj.clgportal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeacherDto {
	private long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String username;
	private String email;
	private String password;
	private String profilePic;
	private Character status;
}
