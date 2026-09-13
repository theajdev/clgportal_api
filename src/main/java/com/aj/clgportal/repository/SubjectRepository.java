package com.aj.clgportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aj.clgportal.dto.SubjectDto;
import com.aj.clgportal.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
	public boolean existsBySubject(String subject);

	public List<Subject> findByStatus(Character status);
}
