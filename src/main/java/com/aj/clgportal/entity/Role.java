package com.aj.clgportal.entity;

import java.util.jar.Attributes.Name;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "tbl_role", uniqueConstraints = { @UniqueConstraint(name="UK_role_roledesc" ,columnNames = "roleDesc")})
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl_role_seq")
	@SequenceGenerator(name = "tbl_role_seq", sequenceName = "tbl_role_seq", allocationSize = 1)
	@Column(name = "role_id")
	private long id;
	@Column(name = "role_desc", nullable = false, length = 20, unique = true)
	private String roleDesc;
	@Column(name = "display_name", nullable = false, length = 20)
	private String roleDisp;
	@Column(name = "role_status", nullable = false, length = 1)
	private Character status;
}