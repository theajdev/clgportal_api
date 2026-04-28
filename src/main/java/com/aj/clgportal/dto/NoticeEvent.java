package com.aj.clgportal.dto;

import java.util.List;

import com.aj.clgportal.util.NoticeType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoticeEvent {
	private Long noticeId;
    private String title;
    private String description;
    private String sendersUsername;
    private String sendersProfilePic;
    private String sentBy;
    private List<Long> deptIds;
    private NoticeType type;
    private List<AttachmentEventDto> attachments;
    private Long id;
}
