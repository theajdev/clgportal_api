package com.aj.clgportal.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.TeacherDto;
import com.aj.clgportal.entity.Department;
import com.aj.clgportal.entity.Role;
import com.aj.clgportal.entity.Teacher;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.DeptRespository;
import com.aj.clgportal.repository.RoleRepository;
import com.aj.clgportal.repository.TeacherRepository;
import com.aj.clgportal.service.TeacherService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

	@Autowired
	TeacherRepository teacherRepo;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	DeptRespository deptRepo;

	@Autowired
	RoleRepository roleRepo;

	private PasswordEncoder passwordEncoder;

	@Override
	public TeacherDto newTeacher(TeacherDto teacherDto) {
		
		Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = formatter.format(currentDate);
        
        Date postedDate=null;
		try {
			postedDate = formatter.parse(formattedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Department department = deptRepo.findById(teacherDto.getDeptId()).orElseThrow(
				() -> new ResourceNotFoundException("Department", "department id", teacherDto.getDeptId()));
		Teacher teacher = new Teacher();
		teacher.setFirstName(teacherDto.getFirstName());
		teacher.setMiddleName(teacherDto.getMiddleName());
		teacher.setLastName(teacherDto.getLastName());
		teacher.setMobileNo(teacherDto.getMobileNo());
		teacher.setAddress(teacherDto.getAddress());
		teacher.setEmail(teacherDto.getEmail());
		teacher.setUsername(teacherDto.getUsername());
		teacher.setDesignation(teacherDto.getDesignation());
		teacher.setAbout(teacherDto.getAbout());
		teacher.setPostedOn(postedDate);
		teacher.setUpdatedOn(null);
		teacher.setPassword(passwordEncoder.encode(teacherDto.getPassword()));
		teacher.setProfilePic(teacherDto.getProfilePic());
		teacher.setStatus(teacherDto.getStatus());
		List<Role> roles = new ArrayList<>();
		Role userRole = roleRepo.findByName("ROLE_TEACHER");
		roles.add(userRole);
		teacher.setRoles(roles);
		teacher.setDepts(department);
		teacherDto.setDeptId(department.getId());
		Teacher newTeacher = teacherRepo.save(teacher);
		TeacherDto teacherToDto = TeacherToDto(newTeacher);
		teacherToDto.setDeptId(teacher.getDepts().getId());
		return teacherToDto;
	}

	@Override
	public TeacherDto updateTeacher(TeacherDto teacherDto, long id) {
		
		Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = formatter.format(currentDate);
        
        Date updatedDate=null;
		try {
			updatedDate = formatter.parse(formattedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Department department = deptRepo.findById(teacherDto.getDeptId()).orElseThrow(
				() -> new ResourceNotFoundException("Department", "department id", teacherDto.getDeptId()));
		Teacher teacher = teacherRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher", "teacher id", id));
		
		teacher.setFirstName(teacherDto.getFirstName());
		teacher.setMiddleName(teacherDto.getMiddleName());
		teacher.setLastName(teacherDto.getLastName());
		teacher.setMobileNo(teacherDto.getMobileNo());
		teacher.setAddress(teacherDto.getAddress());
		teacher.setEmail(teacherDto.getEmail());
		teacher.setUsername(teacherDto.getUsername());
		teacher.setDesignation(teacherDto.getDesignation());
		teacher.setAbout(teacherDto.getAbout());
		teacher.setPostedOn(teacherDto.getPostedOn());
		teacher.setUpdatedOn(updatedDate);
		teacher.setProfilePic(teacherDto.getProfilePic());
		
		/*
		 * if(teacherDto.getPassword() != null && !teacherDto.getPassword().isEmpty()) {
		 * teacher.setPassword(passwordEncoder.encode(teacherDto.getPassword())); }
		 */
		
		teacher.setStatus(teacherDto.getStatus());

		List<Role> roles = new ArrayList<>();
		Role userRole = roleRepo.findByName("ROLE_TEACHER");
		roles.add(userRole);
		teacher.setRoles(roles);
		teacher.setDepts(department);
		Teacher updatedTeacher = teacherRepo.save(teacher);
		TeacherDto teacherToDto = TeacherToDto(updatedTeacher);
		teacherToDto.setDeptId(teacher.getDepts().getId());
		return teacherToDto;
	}

	@Override
	public void deleteTeacher(long id) {
		Teacher teacher = teacherRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher", "teacher id", id));
		teacherRepo.delete(teacher);
	}

	@Override
	public TeacherDto getTeacherById(long id) {
		Teacher teacher = teacherRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher", "teacher id", id));

		TeacherDto teacherToDto = TeacherToDto(teacher);
		teacherToDto.setDeptId(teacher.getDepts().getId());
		return teacherToDto;
	}

	@Override
	public List<TeacherDto> getAllTeachers() {
		 return teacherRepo.findAll().stream()
		            .map(teacher -> {
		                TeacherDto dto = TeacherToDto(teacher);
		                dto.setDeptId(teacher.getDepts().getId());
		                return dto;
		            })
		            .collect(Collectors.toList());
	}
	
	@Override
	public Long getMaxTeacherId() {
		Long maxRoleId = teacherRepo.findMaxTeacherId();
		return maxRoleId;
	}

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	@Override
	public void resetTeacherSequence(Long nextVal) { 
	    String sql = "ALTER SEQUENCE tbl_teacher_seq RESTART WITH " + nextVal;
	    entityManager.createNativeQuery(sql).executeUpdate();
	}
	
	@Transactional
	@Override
	public void removeTeacherRole(Long id) {
		String sql = "DELETE FROM teacher_roles WHERE teacher_id = :teacherId";
	    entityManager.createNativeQuery(sql)
	        .setParameter("teacherId", id)
	        .executeUpdate();
	}
	
	@Override
	public List<TeacherDto> getTeacherByStatus(Character status) {
		
		return teacherRepo.findByStatus(status).stream()
	            .map(teacher -> {
	                TeacherDto dto = TeacherToDto(teacher);
	                dto.setDeptId(teacher.getDepts().getId());
	                return dto;
	            })
	            .collect(Collectors.toList());
}

	public TeacherDto TeacherToDto(Teacher teacher) {
		TeacherDto teacherDto = modelMapper.map(teacher, TeacherDto.class);
		return teacherDto;
	}

	public Teacher DtoToTeacher(TeacherDto teacherDto) {
		Teacher teacher = modelMapper.map(teacherDto, Teacher.class);
		return teacher;
	}

	@Override
	public Long getTeacherCount(Character status) {
		Long teacherCount = teacherRepo.countByStatus(status);
		return teacherCount;
	}
}
