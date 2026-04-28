package com.aj.clgportal.entity;

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
@Table(name = "tbl_notice_attachment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeAttachment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String fileName;
	private String contentType;
	private Long size;
	private String filePath;
	private String fileUrl;
	@ManyToOne
	@JoinColumn(name = "notice_id")
	private Notice notice;
}
