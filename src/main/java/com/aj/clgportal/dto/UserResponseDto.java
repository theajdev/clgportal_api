package com.aj.clgportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
	 private Long id;
	    private String name;
	    private String email;
	    private String type; // "ADMIN", "TEACHER", "STUDENT"
	    private String token;
}
