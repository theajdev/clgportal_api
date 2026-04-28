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
public class NoticeReadDto {
	public Long id;
	
	public Long noticeId;
	
	private Long studentId;

	private Long teacherId;

	private Date readAt;
}
