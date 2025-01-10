package com.aj.clgportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aj.clgportal.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	Optional<Teacher> findByUsername(String username);

	Boolean existsByEmail(String email);

	Teacher findByUsernameOrEmail(String username, String email);

	Boolean existsByUsername(String username);
}