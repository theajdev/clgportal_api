package com.aj.clgportal.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl_student_seq")
	@SequenceGenerator(name = "tbl_student_seq", sequenceName = "tbl_student_seq", allocationSize = 1)
	@Column(name = "student_id")
	private long id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "middle_name")
	private String middleName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "username")
	private String username;
	@Column(name = "email")
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "guardian_name")
	private String guardianName;
	@Column(name = "profile_pic")
	private String profilePic;
	@Column(name = "status")
	private Character status;
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
	@ManyToOne
	@JoinColumn(name = "dept_id")
	private Department depts;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "student_roles", joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "student_id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
	List<Role> roles;
	@ManyToOne
	@JoinColumn(name="guardian_notice_id")
	private GuardianNotice guardianNotice;
	
}
