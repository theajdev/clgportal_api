package com.aj.clgportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aj.clgportal.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	Optional<Student> findByUsername(String username);

	Boolean existsByEmail(String email);

	Student findByUsernameOrEmail(String username, String email);

	Boolean existsByUsername(String username);
}
