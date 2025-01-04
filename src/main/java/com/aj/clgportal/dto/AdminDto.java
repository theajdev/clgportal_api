package com.aj.clgportal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminDto {
	public long id;
	public String firstName;
	public String lastName;
	public String middleName;
	public String username;
	public String email;
	public String password;
	public String profilePic;
	public Character status;
}
