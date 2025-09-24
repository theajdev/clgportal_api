package com.aj.clgportal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeDto {
	private Long id;
	private String noticeTitle;
	private String noticeDesc;
	private Character status;
	private long deptId;
}
