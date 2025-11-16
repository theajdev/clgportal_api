package com.aj.clgportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aj.clgportal.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	Optional<Student> findByUsername(String username);

	Boolean existsByEmail(String email);

	Student findByUsernameOrEmail(String username, String email);

	Boolean existsByUsername(String username);
	
	@Query(value = "select COALESCE(max(s.student_id),0) from tbl_student s",nativeQuery = true)
	Long findMaxStudentId();
	
	List<Student> findByStatus(Character status);
}
