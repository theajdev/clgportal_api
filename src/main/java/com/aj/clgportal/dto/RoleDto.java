package com.aj.clgportal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
	public long id;
	@NotNull
	@Size(min = 4,max = 20,message = "User Type must be contains minimum 4 characters.")
	public String roleDesc;
	private String roleDisp;
	@NotNull
	public Character status;
}
