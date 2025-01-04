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
@Table(name="tbl_admin")
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl_admin_seq")
	@SequenceGenerator(name = "tbl_admin_seq", sequenceName = "tbl_admin_seq", allocationSize = 1)
	@Column(name="admin_id")
	public long id;
	@Column(name="first_name",nullable = false)
	public String firstName;
	@Column(name="middle_name",nullable = true)
	public String middleName;
	@Column(name="last_name",nullable = false)
	public String lastName;
	@Column(name="username",nullable = false)
	public String username;
	@Column(name="email",nullable = false)
	public String email;
	@Column(name="password",nullable = false)
	public String password;
	@Column(name="profile_pic",nullable = true)
	public String profilePic;
	@Column(name="status",nullable = false)
	public Character status;
	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinTable(name="users_roles",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "admin_id"),
	inverseJoinColumns = @JoinColumn(name="role_id",referencedColumnName = "role_id"))
	private List<Role> roles;
}
