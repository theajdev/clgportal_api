package com.aj.clgportal.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_notice_reply")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeReply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "message", nullable = false, length = 300)
	private String message;

	@Column(name = "replied_by")
	private String repliedBy;

	@Column(name = "username")
	private String username;

	@Column(name = "profile_pic")
	private String profilePic;

	@Column(name = "designation")
	private String designation;

	@Column(name = "replied_on")
	private Date repliedOn;

	@ManyToOne
	@JoinColumn(name = "notice_id")
	@JsonIgnore // 🔥 ADD THIS
	private Notice notice;
}
