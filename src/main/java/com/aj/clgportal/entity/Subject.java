package com.aj.clgportal.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "tbl_subject", uniqueConstraints = { @UniqueConstraint(name="UK_subject_subjectdesc" ,columnNames = "subjectDesc")})
public class Subject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl_subject_seq")
	@SequenceGenerator(name = "tbl_subject_seq", sequenceName = "tbl_subject_seq", allocationSize = 1)
	@Column(name = "subject_id")
	private long id;
	@Column(name = "subject_desc", nullable = false, length = 100, unique = true)
	private String subject;
	@Column(name = "sub_status", nullable = false, length = 1)
	private Character status;
	@ManyToMany(mappedBy = "subjects")
	private Set<Student> students = new HashSet<>();

}
