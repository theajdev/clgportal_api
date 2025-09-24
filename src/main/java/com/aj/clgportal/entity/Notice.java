package com.aj.clgportal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_notice",uniqueConstraints = {
		@UniqueConstraint(name="UK_notice_title",columnNames ="noticeTitle")
})
public class Notice {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="tbl_notice_seq")
	@SequenceGenerator(name = "tbl_notice_seq", allocationSize = 1, sequenceName = "tbl_notice_seq")
	@Column(name = "notice_id")
	private Long id;
	@Column(name = "notice_title", nullable = false, length = 40, unique = true)
	private String noticeTitle;
	@Column(name = "notice_desc", nullable = false, length = 200, unique = true)
	private String noticeDesc;
	@Column(name = "status", nullable = false, length = 1)
	private Character status;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dept_id")
	private Department depts;
}
