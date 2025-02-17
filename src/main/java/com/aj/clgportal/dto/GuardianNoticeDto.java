package com.aj.clgportal.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GuardianNoticeDto {
	private long id;
	private Date creationDate;
	private String noticeDesc;
	private String noticeTitle;
	private Character status;
	private long studentId;
	private long teacherId;
}
