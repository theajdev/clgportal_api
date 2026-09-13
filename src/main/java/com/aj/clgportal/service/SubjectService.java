package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.dto.DeleteSelectedIdsResponseDto;
import com.aj.clgportal.dto.SubjectDto;
import com.aj.enums.ModuleName;

public interface SubjectService {
	public SubjectDto newSubject(SubjectDto subjectDto);
	public SubjectDto editSubject(Long subjectId,SubjectDto subjectDto);
	public void removeSubject(Long subjectId);
	public SubjectDto viewSubject(Long subjectId);
	public List<SubjectDto> viewAllSubjects();
	public List<SubjectDto> getSubjectByStatus(Character status);
	public List<DeleteSelectedIdsResponseDto> deleteSelectedSubjects(ModuleName module,
	        List<Long> subjectIds);
}
