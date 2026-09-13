package com.aj.clgportal.entity;

import java.util.HashSet;
import java.util.Set;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "role_id")
	private long id;
	@Column(name = "role_name", nullable = false, length = 20, unique = true)
	private String roleName;
	@Column(name = "role_display")
	private String roleDisp;
	
	@Column(name = "is_active")
	private Boolean isActive = true;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "role_permissions",
	    joinColumns = @JoinColumn(name =   "role_id"),
	    inverseJoinColumns = @JoinColumn(name = "permission_id")
	)
	private Set<ApiPermission> permissions = new HashSet<>();

}