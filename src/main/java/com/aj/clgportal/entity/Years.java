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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_years")
public class Years {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl_year_seq")
	@SequenceGenerator(name = "tbl_year_seq", sequenceName = "tbl_year_seq", allocationSize = 1)
	@Column(name = "year_id")
	private long id;
	@Column(name="year_Desc",nullable = false)
	private String yearDesc;
	@Column(name="status",nullable = false)
	private Character status;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "dept_years", joinColumns = @JoinColumn(name = "year_id", referencedColumnName = "year_id"), inverseJoinColumns = @JoinColumn(name = "dept_id", referencedColumnName = "dept_id"))
	private List<Department> departments;
}
