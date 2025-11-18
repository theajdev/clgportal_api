package com.aj.clgportal.serviceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.StudentDto;
import com.aj.clgportal.dto.TeacherDto;
import com.aj.clgportal.entity.Department;
import com.aj.clgportal.entity.Role;
import com.aj.clgportal.entity.Student;
import com.aj.clgportal.entity.Teacher;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.DeptRespository;
import com.aj.clgportal.repository.RoleRepository;
import com.aj.clgportal.repository.StudentRepository;
import com.aj.clgportal.repository.TeacherRepository;
import com.aj.clgportal.service.StudentService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	TeacherRepository teacherRepo;
	
	@Autowired
	DeptRespository deptRepo;
	
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Override
	public StudentDto newStudent(StudentDto studDto,HttpSession session)  {
		Department department = deptRepo.findById(studDto.getDeptId()).orElseThrow(()->new ResourceNotFoundException("Department", "department id", studDto.getDeptId()));
		String teacherUsername=SecurityContextHolder.getContext()
		        .getAuthentication()
		        .getName();
		Student student = new Student();
		student.setFirstName(studDto.getFirstName());
		student.setMiddleName(studDto.getMiddleName());
		student.setLastName(studDto.getLastName());
		student.setUsername(studDto.getUserName());
		student.setEmail(studDto.getEmail());
		student.setPassword(passwordEncoder.encode(studDto.getPassword()));
		student.setGuardianName(studDto.getGuardianName());
		student.setProfilePic(studDto.getProfilePic());
		student.setStatus(studDto.getStatus());
		Teacher teacher=teacherRepo.findByUsernameOrEmail(teacherUsername, teacherUsername);
		student.setTeacher(teacher);
		student.setDepts(department);
		List<Role> roles = new ArrayList<>();
		Role userRole = roleRepo.findByName("ROLE_STUDENT");
		roles.add(userRole);
		student.setRoles(roles);

		Student newStudent = studentRepo.save(student);
		StudentDto stud = StudentToDto(newStudent);
		return stud;
	}

	@Override
	public StudentDto updateStudent(StudentDto studDto, long id,HttpSession session) {
		Department department = deptRepo.findById(studDto.getDeptId()).orElseThrow(()->new ResourceNotFoundException("Department", "department id", studDto.getDeptId()));
		String teacherUsername=SecurityContextHolder.getContext()
		        .getAuthentication()
		        .getName();
		Student student = studentRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "student id", id));
		student.setFirstName(studDto.getFirstName());
		student.setMiddleName(studDto.getMiddleName());
		student.setLastName(studDto.getLastName());
		student.setUsername(studDto.getUserName());
		student.setEmail(studDto.getEmail());
		if(studDto.getPassword() != null && !studDto.getPassword().isEmpty()) {
			student.setPassword(passwordEncoder.encode(studDto.getPassword()));
		}
		student.setGuardianName(studDto.getGuardianName());
		student.setProfilePic(studDto.getProfilePic());
		student.setStatus(studDto.getStatus());
		Teacher teacher=teacherRepo.findByUsernameOrEmail(teacherUsername, teacherUsername);
		student.setTeacher(teacher);
		student.setDepts(department);
		List<Role> roles = new ArrayList<>();
		Role userRole = roleRepo.findByName("ROLE_STUDENT");
		roles.add(userRole);
		student.setRoles(roles);
		Student updatedStudent = studentRepo.save(student);
		StudentDto stud = StudentToDto(updatedStudent);
		return stud;
	}

	@Override
	public void deleteStudent(long id) {
		Student student = studentRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "student id", id));
		studentRepo.delete(student);
	}

	@Override
	public StudentDto getStudentById(long id) {
		Student student = studentRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "student id", id));
		StudentDto studentToDto = StudentToDto(student);
		studentToDto.setDeptId(student.getDepts().getId());
		return studentToDto;
	}

	@Override
	public List<StudentDto> getAllStudents() {
		return studentRepo.findAll().stream()
	            .map(student -> {
	                StudentDto dto = StudentToDto(student);
	                dto.setDeptId(student.getDepts().getId());
	                return dto;
	            })
	            .collect(Collectors.toList());
	}

	public StudentDto StudentToDto(Student stud) {
		StudentDto studentDto = modelMapper.map(stud, StudentDto.class);
		return studentDto;
	}

	public Student DtoToStudent(StudentDto studDto) {
		Student student = modelMapper.map(studDto, Student.class);
		return student;
	}

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	@Override
	public void removeStudentRole(Long id) {
		String sql = "DELETE FROM student_roles WHERE student_id = :studentId";
	    entityManager.createNativeQuery(sql)
	        .setParameter("studentId", id)
	        .executeUpdate();
	}

	@Override
	public Long getMaxStudentId() {
		Long maxRoleId = studentRepo.findMaxStudentId();
		return maxRoleId;
	}

	@Transactional
	@Override
	public void resetStudentSequence(Long nextVal) {
		String sql = "ALTER SEQUENCE tbl_student_seq RESTART WITH " + nextVal;
	    entityManager.createNativeQuery(sql).executeUpdate();
		
	}
	
	@Override
	public List<StudentDto> getStudentByStatus(Character status) {
		List<Student> list = studentRepo.findByStatus(status);
		List<StudentDto> lst = list.stream().map(student -> StudentToDto(student)).collect(Collectors.toList());
		
		list.forEach(student -> {
			// Assuming getDepts() returns a Department object and getId() returns the
			// department ID
			lst.forEach(studDto -> {
				studDto.setDeptId(student.getDepts().getId());
			});
		});
		lst.sort(Comparator.comparing(StudentDto::getId));
		return lst;
	}

	

	

}
