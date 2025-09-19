package com.aj.clgportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aj.clgportal.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	Optional<Teacher> findByUsername(String username);

	Boolean existsByEmail(String email);

	Teacher findByUsernameOrEmail(String username, String email);

	Boolean existsByUsername(String username);
	
	List<Teacher> findByStatus(Character str);
	
	@Query(value = "select COALESCE(max(t.teacher_id),0) from tbl_teacher t",nativeQuery = true)
	Long findMaxTeacherId();
	
}