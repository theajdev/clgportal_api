package com.aj.clgportal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_user")
public class UserType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl_user_seq")
	@SequenceGenerator(name = "tbl_user_seq", sequenceName = "tbl_user_seq", allocationSize = 1)
	@Column(name="user_id")
	private long id;
	@Column(name="user_desc",nullable = false,length = 10)
	private String userDesc;
	@Column(name="user_status",nullable = false,length = 1)
	private Character status;

}
