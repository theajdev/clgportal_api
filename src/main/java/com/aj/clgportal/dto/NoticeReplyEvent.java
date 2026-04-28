package com.aj.clgportal.dto;

import java.util.Date;

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
    private String senderName;
    private String profilePic;
    private Date repliedOn;
}