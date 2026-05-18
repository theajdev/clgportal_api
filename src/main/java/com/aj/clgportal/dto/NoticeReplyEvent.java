package com.aj.clgportal.dto;

import java.util.Date;
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
public class NoticeReplyEvent {
    private Long noticeId;
    private String message;
    private String repliedBy;
    private String username;
    private String profilePic;
    private Date repliedOn;
    private Long notificationId;
    private NoticeType type;
    private List<Long> deptIds;
}