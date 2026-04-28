package com.aj.clgportal.dto;

import java.util.Date;

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
	public long mobileNo;
	public String address;
	public String email;
	public String password;
	public String profilePic;
	public Character status;
	private String designation;
	private String about;
	private Date postedOn;
	private Date updatedOn;
	
}
