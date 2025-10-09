package com.aj.clgportal.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	    name = "tbl_notice_department",
	    joinColumns = @JoinColumn(name = "notice_id"),
	    inverseJoinColumns = @JoinColumn(name = "dept_id")
	)
	private List<Department> depts;
	@Column(name="posted_on")
	private Date postedOn;
	@Column(name="updated_on")
	private Date updatedOn;
}
