package com.aj.clgportal.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.DeleteSelectedIdsResponseDto;
import com.aj.clgportal.dto.SubjectDto;
import com.aj.clgportal.entity.Subject;
import com.aj.clgportal.exception.DuplicateResourceException;
import com.aj.clgportal.exception.ResourceAlreadyExistsException;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.SubjectRepository;
import com.aj.clgportal.service.SubjectService;
import com.aj.enums.ModuleName;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class SubjectServiceImpl implements SubjectService {
	
	@Autowired
	SubjectRepository subjectRepo;
	
	@Autowired
	ModelMapper mapper;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SubjectDto newSubject(SubjectDto subjectDto) {
		// Check if title already exists
				if (subjectRepo.existsBySubject(subjectDto.getSubject())) {
					throw new DuplicateResourceException(subjectDto.getSubject() + " subject already exists.");
				}else {
				
				subjectRepo.existsBySubject(subjectDto.getSubject());
				Subject subject = mapper.map(subjectDto, Subject.class);
				Subject savedSubject = subjectRepo.save(subject);
				return subjectToDto(savedSubject);
				}
		
	}

	@Override
	public SubjectDto editSubject(Long subjectId, SubjectDto subjectDto) {
		Subject subject = subjectRepo.findById(subjectId).orElseThrow(()->new ResourceNotFoundException("Subject","subject id",String.valueOf(subjectId)));
		subject.setSubject(subjectDto.getSubject());
		subject.setStatus(subjectDto.getStatus());
		Subject updatedSubject = subjectRepo.save(subject);
		return subjectToDto(updatedSubject);
	}

	@Override
	public void removeSubject(Long subjectId) {
		Subject subject = subjectRepo.findById(subjectId).orElseThrow(()->new ResourceNotFoundException("Subject","subject id",String.valueOf(subjectId)));
		subjectRepo.delete(subject);

	}

	@Override
	public SubjectDto viewSubject(Long subjectId) {
		Subject subject = subjectRepo.findById(subjectId).orElseThrow(()->new ResourceNotFoundException("Subject","subject id",String.valueOf(subjectId)));
		SubjectDto subjectDto = subjectToDto(subject);
		return subjectDto;
	}

	@Override
	public List<SubjectDto> viewAllSubjects() {
		List<Subject> subjects = subjectRepo.findAll();
		List<SubjectDto> subjectDto = subjects.stream().map((subject)->subjectToDto(subject)).collect(Collectors.toList());
		return subjectDto;
	}
	
	@Override
	public List<SubjectDto> getSubjectByStatus(Character status) {
		List<Subject> subjects = subjectRepo.findByStatus(status);
		List<SubjectDto> subjectDto = subjects.stream().map((subject)->subjectToDto(subject)).collect(Collectors.toList());
		return subjectDto;
	}
	
	@Override
	public List<DeleteSelectedIdsResponseDto> deleteSelectedSubjects(ModuleName module,
	        List<Long> subjectIds) {
		
		Query query = entityManager.createNativeQuery(
		        "SELECT * FROM delete_selected_ids(:module, CAST(:ids AS BIGINT[]))"
		    );
		
		query.setParameter("module", module.name());
	    query.setParameter("ids", subjectIds.toArray(new Long[0]));
		
	    List<Object[]> results = query.getResultList();

	    return results.stream()
	            .map(obj -> new DeleteSelectedIdsResponseDto(
	                    ((Number) obj[0]).longValue(),
	                    (String) obj[1]
	            ))
	            .toList();
	}
	
	public SubjectDto subjectToDto(Subject subject) {
		SubjectDto subjectDto = mapper.map(subject, SubjectDto.class);
		return subjectDto;
	}
	
	public Subject dtoToSubject(SubjectDto subjectDto) {
		Subject subject = mapper.map(subjectDto, Subject.class);
		return subject;
	}

	

	

}
