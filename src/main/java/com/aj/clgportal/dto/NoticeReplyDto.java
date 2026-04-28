package com.aj.clgportal.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeReplyDto {
	private Long id;
	private String message;
	private String repliedBy;
	private String profilePic;
	private String designation;
	private Date repliedOn;
}
