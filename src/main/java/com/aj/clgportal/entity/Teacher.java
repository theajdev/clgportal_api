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
@Table(name = "tbl_teacher")
public class Teacher {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl_teacher_seq")
	@SequenceGenerator(name = "tbl_teacher_seq", sequenceName = "tbl_teacher_seq", allocationSize = 1)
	@Column(name = "teacher_id")
	private long id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "middle_name")
	private String middleName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "mobile_no",length = 10)
	private long mobileNo;
	@Column(name = "address")
	private String address;
	@Column(name = "username")
	private String username;
	@Column(name = "email")
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "profile_pic")
	private String profilePic;
	@Column(name = "status")
	private Character status;
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	private List<Student> students;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dept_id")
	private Department depts;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "teacher_roles", joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
	private List<Role> roles;
	@ManyToOne
	@JoinColumn(name="guardian_notice_id")
	private GuardianNotice guardianNotice;
}
