package com.aj.clgportal.dto;

import java.util.Date;
import java.util.List;

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
	private List<Long> deptId;
	private Date postedOn;
	private Date updatedOn;
}