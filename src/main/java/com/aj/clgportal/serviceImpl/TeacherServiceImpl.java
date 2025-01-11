package com.aj.clgportal.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.DepartmentDto;
import com.aj.clgportal.dto.TeacherDto;
import com.aj.clgportal.entity.Department;
import com.aj.clgportal.entity.Role;
import com.aj.clgportal.entity.Teacher;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.DeptRespository;
import com.aj.clgportal.repository.RoleRepository;
import com.aj.clgportal.repository.TeacherRepository;
import com.aj.clgportal.service.TeacherService;

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
		Department department = deptRepo.findById(teacherDto.getDeptId()).orElseThrow(
				() -> new ResourceNotFoundException("Department", "department id", teacherDto.getDeptId()));
		Teacher teacher = new Teacher();
		teacher.setFirstName(teacherDto.getFirstName());
		teacher.setMiddleName(teacherDto.getMiddleName());
		teacher.setLastName(teacherDto.getLastName());
		teacher.setEmail(teacherDto.getEmail());
		teacher.setUsername(teacherDto.getUsername());
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
		Department department = deptRepo.findById(teacherDto.getDeptId()).orElseThrow(
				() -> new ResourceNotFoundException("Department", "department id", teacherDto.getDeptId()));
		Teacher teacher = teacherRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher", "teacher id", id));
		teacher.setFirstName(teacherDto.getFirstName());
		teacher.setMiddleName(teacherDto.getMiddleName());
		teacher.setLastName(teacherDto.getLastName());
		teacher.setEmail(teacherDto.getEmail());
		teacher.setUsername(teacherDto.getUsername());
		teacher.setProfilePic(teacherDto.getProfilePic());
		teacher.setPassword(passwordEncoder.encode(teacherDto.getPassword()));
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
		List<Teacher> all = teacherRepo.findAll();
		List<TeacherDto> lst = all.stream().map((list) -> TeacherToDto(list)).collect(Collectors.toList());
		// Set the department ID in each TeacherDto based on the teacher's department
		all.forEach(teacher -> {
			// Assuming getDepts() returns a Department object and getId() returns the
			// department ID
			lst.forEach(teachDto -> {
				teachDto.setDeptId(teacher.getDepts().getId());
			});
		});
		return lst;
	}

	public TeacherDto TeacherToDto(Teacher teacher) {
		TeacherDto teacherDto = modelMapper.map(teacher, TeacherDto.class);
		return teacherDto;
	}

	public Teacher DtoToTeacher(TeacherDto teacherDto) {
		Teacher teacher = modelMapper.map(teacherDto, Teacher.class);
		return teacher;
	}

}
