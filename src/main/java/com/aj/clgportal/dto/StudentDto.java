package com.aj.clgportal.dto;

import java.util.Date;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentDto {
	private long id;
	@NotBlank
	@Size(min = 4,max = 100)
	private String firstName;
	private String middleName;
	@NotBlank
	@Size(min = 4,max = 100)
	private String lastName;
	@NotNull
	@Min(value = 1000000000L)
	@Max(value = 9999999999L)
	public long mobileNo;
	@NotBlank
	public String address;
	@NotBlank
	private String username;
	@NotBlank
	private String email;
	@NotBlank
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one number and one special character"
    )
	private String password;
	private String guardianName;
	private String profilePic;
	@NotNull
	private Character status;
	private String about;
	private Date postedOn;
	private Date updatedOn;
	private TeacherDto teacher;
	private long deptId;
}
