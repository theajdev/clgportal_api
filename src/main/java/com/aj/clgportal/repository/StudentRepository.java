package com.aj.clgportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aj.clgportal.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
