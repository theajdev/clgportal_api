package com.aj.clgportal.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_department",uniqueConstraints = {
		@UniqueConstraint(name="UK_dept_desc",columnNames = "deptDesc")
})
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl_dept_seq")
	@SequenceGenerator(name = "tbl_dept_seq", sequenceName = "tbl_dept_seq", allocationSize = 1)
	@Column(name = "dept_id")
	private long id;
	@Column(name = "dept_desc", nullable = false,unique = true)
	private String deptDesc;
	@Column(name = "status", nullable = false)
	private Character status;
	@OneToMany(mappedBy = "depts", cascade = CascadeType.ALL)
	private List<Teacher> teachers;
	@OneToMany(mappedBy = "depts", cascade = CascadeType.ALL)
	private List<Student> students;
	@ManyToMany(mappedBy = "depts", cascade = CascadeType.ALL)
	private List<Notice> notices;
}
