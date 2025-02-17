package com.aj.clgportal.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_guardian_notice")
public class GuardianNotice {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl_guardian_notice_seq")
	@SequenceGenerator(name = "tbl_guardian_notice_seq", sequenceName = "tbl_guardian_notice_seq", allocationSize = 1)
	@Column(name = "guardian_notice_id")
	private long id;
	@Column(name = "notice_desc")
	private String noticeDesc;
	@Column(name = "notice_title")
	private String noticeTitle;
	@Column(name = "status")
	private Character status;
	@Column(name = "creation_date")
	private Date creationDate;
	@OneToMany(mappedBy = "guardianNotice", fetch = FetchType.LAZY)
	private List<Student> students;
	@OneToMany(mappedBy = "guardianNotice", fetch = FetchType.LAZY)
	private List<Teacher> teachers;
}
