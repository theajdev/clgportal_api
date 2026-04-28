package com.aj.clgportal.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String title;
	private String message;
	private String sentBy;
    private Long departmentId; // ✅ IMPORTANT
    @Column(name="senders_username")
    private String sendersUsername;
    @Column(name="senders_profile_pic")
    private String sendersProfilePic;
    @Column(name = "read", nullable = false)
    private boolean read;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "content_type")
	private String contentType;
    @Column(name = "size")
	private Long size;
    @Column(name = "file_url")
	private String fileUrl;
    private Long noticeId;
}
